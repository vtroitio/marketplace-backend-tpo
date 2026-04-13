package com.uade.tpo.grupo7.marketplace.users.seeder;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.uade.tpo.grupo7.marketplace.common.enums.RoleCode;
import com.uade.tpo.grupo7.marketplace.users.entity.Role;
import com.uade.tpo.grupo7.marketplace.users.repository.RoleRepository;

@Component
@Order(1)
public class RoleSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;

    private final List<Role> roles = List.of(
            Role.builder()
                .name("Super Administrador")
                .code(RoleCode.SUPER_ADMIN)
                .build(),
            Role.builder()
                .name("Administrador")
                .code(RoleCode.ADMIN)
                .build(),
            Role.builder()
                .name("Comprador")
                .code(RoleCode.BUYER)
                .build(),
            Role.builder()
                .name("Vendedor")
                .code(RoleCode.SELLER)
                .build());

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
