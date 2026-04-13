package com.uade.tpo.grupo7.marketplace.users.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.uade.tpo.grupo7.marketplace.common.enums.RoleCode;
import com.uade.tpo.grupo7.marketplace.users.entity.Role;
import com.uade.tpo.grupo7.marketplace.users.entity.User;
import com.uade.tpo.grupo7.marketplace.users.repository.RoleRepository;
import com.uade.tpo.grupo7.marketplace.users.repository.UserRepository;

@Component
@Order(2)
public class UserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.existsByUsername("super_admin")) {
            return;
        }

        Role superAdminRole = roleRepository.findByCode(RoleCode.SUPER_ADMIN)
                .orElseThrow(() -> new RuntimeException(RoleCode.SUPER_ADMIN + " role not found"));

        User superAdminUser = User.builder()
                .username("super_admin")
                .email("super@admin.com")
                .passwordHash(passwordEncoder.encode("password"))
                .name("Super")
                .surname("Admin")
                .role(superAdminRole)
                .build();

        userRepository.save(superAdminUser);
    }

}
