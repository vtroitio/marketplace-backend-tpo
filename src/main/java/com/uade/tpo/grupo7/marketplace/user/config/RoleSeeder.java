package com.uade.tpo.grupo7.marketplace.user.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.uade.tpo.grupo7.marketplace.user.entity.Role;
import com.uade.tpo.grupo7.marketplace.user.repository.RoleRepository;

@Component
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    private final List<Role> roles = List.of(
            Role.builder().name("Super Administrador").code("ROLE_SUPER_ADMIN").build(),
            Role.builder().name("Administrador").code("ROLE_ADMIN").build(),
            Role.builder().name("Comprador").code("ROLE_BUYER").build(),
            Role.builder().name("Vendedor").code("ROLE_SELLER").build());

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        this.roles.forEach(role -> {
            if (!this.roleRepository.existsByCode(role.getCode())) {
                this.roleRepository.save(role);
            }
        });
    }

}
