
package com.chooseyourpath.config;

import com.chooseyourpath.entity.User;
import com.chooseyourpath.model.Role;
import com.chooseyourpath.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AdminBootstrapper implements CommandLineRunner {

    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_BOOTSTRAP_USERNAME:}")
    private String adminUsername;

    @Value("${ADMIN_BOOTSTRAP_PASSWORD:}")
    private String adminPassword;

    public AdminBootstrapper(
            UserRepository users,
            PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Do nothing unless both Railway variables are configured.
        if (adminUsername == null || adminUsername.isBlank()
                || adminPassword == null || adminPassword.isBlank()) {
            return;
        }

        // Never overwrite an existing account.
        if (users.existsByUsername(adminUsername)) {
            System.out.println(
                    "Admin bootstrap skipped: username already exists.");
            return;
        }

        User admin = new User();
        admin.setUsername(adminUsername);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole(Role.ADMIN);

        users.save(admin);

        System.out.println("Admin account created successfully.");
    }
}
