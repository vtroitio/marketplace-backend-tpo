package com.uade.tpo.grupo7.marketplace.users.dto;

public record UpdateUserRequest(
    String username,
    String name,
    String surname,
    String email
) {}
