package com.example.hotel.service;

import com.example.hotel.aop.AuthoriseUserCreateByAnonymous;
import com.example.hotel.aop.AuthoriseUserUpdateAndDelete;
import com.example.hotel.entity.RoleType;
import com.example.hotel.entity.User;
import com.example.hotel.event.UserRegistrationEvent;
import com.example.hotel.exception.EntityNotFoundException;
import com.example.hotel.exception.UserAlreadyExistsException;
import com.example.hotel.mapper.UserMapper;
import com.example.hotel.repository.UserRepository;
import com.example.hotel.repository.UserSpecification;
import com.example.hotel.web.dto.v1.UserFilter;
import com.example.hotel.web.dto.v1.UserUpsertRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.hotel.service.MessageTemplates.TEMPLATE_USER_ALREADY_EXISTS_EXCEPTION;
import static com.example.hotel.service.MessageTemplates.TEMPLATE_USER_NOT_FOUND_EXCEPTION;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    @Value("${app.kafka.kafkaStatisticsService.kafkaUserCreatedStatus}")
    private String kafkaUserCreatedStatus;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public List<User> findAll(UserFilter userFilter) {
        return userRepository.fetchAll(
                UserSpecification.withFilter(userFilter),
                PageRequest.of(
                        userFilter.pageNumber(),
                        userFilter.pageSize()
                )
        ).getContent();
    }

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(MessageFormat.format(TEMPLATE_USER_NOT_FOUND_EXCEPTION, id)));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Username not found!"));
    }

    @AuthoriseUserCreateByAnonymous
    @Transactional
    public UUID save(User user, RoleType roleType) {
        userRepository.findUserIdByUsernameAndEmail(
                user.getUsername(),
                user.getEmail()
        ).ifPresent(id -> {
                    throw new UserAlreadyExistsException(
                            MessageFormat.format(TEMPLATE_USER_ALREADY_EXISTS_EXCEPTION,
                                    user.getUsername(),
                                    user.getEmail()));
        });

        user.setRoles(new ArrayList<>(List.of(roleType)));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        UUID userId = userRepository.saveAndFlush(user).getId();

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        kafkaTemplate.send(kafkaUserCreatedStatus,
                                new UserRegistrationEvent(userId)
                        );
                    }
                }
        );

        return userId;
    }

    @AuthoriseUserUpdateAndDelete
    @Transactional
    public UUID update(UUID userId, UserUpsertRequest request, RoleType roleType) {
        User existedUser = findById(userId);

        userMapper.updateUser(request, existedUser);

        if (request.password() != null && !request.password().isBlank()) {
            existedUser.setPassword(passwordEncoder.encode(request.password()));
        }

        if (roleType != null) {
            existedUser.getRoles().clear();
            existedUser.getRoles().add(roleType);
        }

        return userRepository.saveAndFlush(existedUser).getId();
    }

    @AuthoriseUserUpdateAndDelete
    @Transactional
    public void delete(UUID id) {
        findById(id);

        userRepository.deleteById(id);
    }
}
