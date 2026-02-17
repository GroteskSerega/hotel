package com.example.hotel.configuration;

import com.example.hotel.entity.Role;
import com.example.hotel.entity.RoleType;
import com.example.hotel.entity.User;
import com.example.hotel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class DatabaseDataInitializer {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Value("${app.security.adminPassword}")
    private String adminPassword;

    @Value("${app.security.adminUsername}")
    private String adminUsername;

    @EventListener(ApplicationReadyEvent.class)
    public void createAdminUserInDb() {
        Optional<User> existedAdmin =
                userRepository.findByUsername(adminUsername);

        if (existedAdmin.isPresent()) {
            return;
        }

        var role = Role.from(RoleType.ROLE_ADMIN);
        var admin = new User();

        admin.setUsername(adminUsername);
        admin.setEmail(adminUsername + "@" + adminUsername + ".com");
        admin.setPassword(passwordEncoder.encode(adminPassword));
        role.setUser(admin);

        userRepository.saveAndFlush(admin);
    }
}
