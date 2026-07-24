package com.uade.tpo.grupo7.marketplace.users.dto;

import com.uade.tpo.grupo7.marketplace.users.entity.Role;

public record UserResponse(
    Long id,
    String username,
    String email,
    String name,
    String surname,
    Role role,
    boolean active
) {}