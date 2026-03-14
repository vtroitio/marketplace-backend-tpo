package com.uade.tpo.grupo9.marketplace.products.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateProductDto(
    @NotBlank(message = "Name is required")
    String name,

    @NotNull(message = "Price is reuired")
    @Positive(message = "Price must be positive and not 0")
    Double price
) {} 
