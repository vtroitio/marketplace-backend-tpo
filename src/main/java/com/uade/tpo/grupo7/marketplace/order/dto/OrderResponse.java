package com.uade.tpo.grupo7.marketplace.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.uade.tpo.grupo7.marketplace.common.enums.OrderStatus;

@Schema(description = "DTO de respuesta para una orden de compra")
public record OrderResponse(
    Long id,
    OrderStatus status,
    BigDecimal totalAmount,
    LocalDateTime createdAt,
    List<OrderItemResponse> items
) {}
