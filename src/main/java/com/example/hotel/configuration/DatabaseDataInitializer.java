package com.example.hotel.configuration;

import com.example.hotel.entity.User;
import com.example.hotel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.example.hotel.entity.RoleType.ROLE_ADMIN;

@RequiredArgsConstructor
@Component
public class DatabaseDataInitializer {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Value("${app.security.adminPassword}")
    private String adminPassword;

    @Value("${app.security.adminUsername}")
    private String adminUsername;

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void createAdminUserInDb() {
        String adminEmail = adminUsername + "@" + adminUsername + ".com";

        Optional<UUID> adminId =
                userRepository.findUserIdByUsernameAndEmail(adminUsername, adminEmail);

        if (adminId.isPresent()) {
            return;
        }

        var admin = new User();

        admin.setUsername(adminUsername);
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRoles(new ArrayList<>(List.of(ROLE_ADMIN)));

        userRepository.saveAndFlush(admin);
    }
}
