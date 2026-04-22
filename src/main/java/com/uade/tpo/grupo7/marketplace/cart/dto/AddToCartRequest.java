package com.uade.tpo.grupo7.marketplace.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para agregar un item al carrito")
public record AddToCartRequest(

    @Schema(description = "ID de la variante del producto", example = "1")
    @NotNull(message = "productVariantId is required")
    Integer productVariantId,

    @Schema(description = "Cantidad a agregar", example = "2")
    @NotNull(message = "quantity is required")
    @Min(value = 1, message = "quantity must be at least 1")
    Integer quantity
) {}
