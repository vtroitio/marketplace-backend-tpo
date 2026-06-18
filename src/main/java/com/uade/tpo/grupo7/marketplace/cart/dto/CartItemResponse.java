package com.uade.tpo.grupo7.marketplace.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "DTO de respuesta para un item del carrito")
public record CartItemResponse(
        Long id,
        Integer productVariantId,
        String name,
        String image,
        VariantAttributeCartResponse size,
        VariantAttributeCartResponse color,
        Integer quantity,
        Integer maxStock,
        BigDecimal price,
        BigDecimal subtotal
) {}
