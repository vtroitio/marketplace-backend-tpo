package com.uade.tpo.grupo7.marketplace.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para actualizar la cantidad de un item del carrito")
public record UpdateCartItemRequest(

    @Schema(description = "Nueva cantidad", example = "3")
    @NotNull(message = "quantity is required")
    @Min(value = 1, message = "quantity must be at least 1")
    Integer quantity
) {}
