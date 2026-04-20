package com.uade.tpo.grupo7.marketplace.users.dto;

public record UserResponse(
    Long id,
    String username,
    String email,
    String name,
    String surname,
    String role,
    boolean active
) {}