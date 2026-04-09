package com.uade.tpo.grupo7.marketplace.users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.grupo7.marketplace.users.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByCode(String code);
}
