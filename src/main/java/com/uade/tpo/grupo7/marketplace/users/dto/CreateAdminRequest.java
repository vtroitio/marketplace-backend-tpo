package com.uade.tpo.grupo7.marketplace.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateAdminRequest(
    @NotBlank(message = "Email is required")
    @Email
    @Size(max = 255)
    String email,

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20)
    @Pattern(
        regexp = "^[a-zA-Z0-9_]+$", 
        message = "Username must contain only letters, numbers and underscores"
    )
    String username,

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100)
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$",
        message = "Password must contain upper, lower and number"
    )
    String password,

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100)
    @Pattern(
        regexp = "^[a-zA-ZÀ-ÿ\\s]+$",
        message = "Name must contain only letters"
    )
    String name,

    @Size(min = 2, max = 100)
    @Pattern(
        regexp = "^[a-zA-ZÀ-ÿ\\s]+$",
        message = "Surname must contain only letters"
    )
    String surname
) {}
