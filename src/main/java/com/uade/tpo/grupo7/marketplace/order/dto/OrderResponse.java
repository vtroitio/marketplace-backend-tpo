package com.uade.tpo.grupo7.marketplace.order.dto;

import com.uade.tpo.grupo7.marketplace.order.entity.OrderStatus;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "DTO de respuesta para una orden de compra")
public record OrderResponse(
    Long id,
    OrderStatus status,
    BigDecimal totalAmount,
    LocalDateTime createdAt,
    List<OrderItemResponse> items
) {}
