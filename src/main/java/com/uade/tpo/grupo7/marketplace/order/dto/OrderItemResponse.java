package com.uade.tpo.grupo7.marketplace.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "DTO de respuesta para un item de orden")
public record OrderItemResponse(
    Long id,
    Integer productVariantId,
    String productName,
    Integer quantity,
    BigDecimal unitPrice,
    BigDecimal totalPrice
) {}
