package com.example.hotel.service;

import com.example.hotel.entity.Role;
import com.example.hotel.entity.User;
import com.example.hotel.exception.EntityNotFoundException;
import com.example.hotel.exception.UserAlreadyExistsException;
import com.example.hotel.mapper.UserMapper;
import com.example.hotel.repository.UserRepository;
import com.example.hotel.repository.UserSpecification;
import com.example.hotel.web.dto.v1.UserFilter;
import com.example.hotel.web.dto.v1.UserUpsertRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public List<User> findAll(UserFilter userFilter) {
        return userRepository.findAll(
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
                        new RuntimeException("Username not found!"));
    }

    @Transactional
    public UUID save(User user, Role role) {
        userRepository.findUserIdByUsernameAndEmail(
                user.getUsername(),
                user.getEmail()
        ).orElseThrow(() ->
                new UserAlreadyExistsException(
                        MessageFormat.format(TEMPLATE_USER_ALREADY_EXISTS_EXCEPTION,
                                user.getUsername(),
                                user.getEmail())));

        user.setRoles(new ArrayList<>(List.of(role)));
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        role.setUser(user);

        return userRepository.saveAndFlush(user).getId();
    }

    @Transactional
    public UUID update(UUID userId, UserUpsertRequest request, Role role) {
        User existedUser = findById(userId);

        userMapper.updateUser(request, existedUser);

        if (request.password() != null && !request.password().isBlank()) {
            existedUser.setPassword(passwordEncoder.encode(request.password()));
        }

        if (role != null) {
            existedUser.getRoles().clear();
            role.setUser(existedUser);
            existedUser.getRoles().add(role);
        }

        return userRepository.saveAndFlush(existedUser).getId();
    }

    @Transactional
    public void delete(UUID id) {
        findById(id);

        userRepository.deleteById(id);
    }
}
