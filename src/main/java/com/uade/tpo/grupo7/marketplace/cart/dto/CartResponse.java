package com.uade.tpo.grupo7.marketplace.cart.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "DTO de respuesta para el carrito")
public record CartResponse(
    Long cartId,
    BigDecimal totalAmount,
    List<CartItemResponse> items
) {}
