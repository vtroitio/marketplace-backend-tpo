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
        BigDecimal subtotalAmount,
        BigDecimal discountAmount,
        BigDecimal shippingCost,
        BigDecimal totalAmount,
        String couponCode,
        String shippingFullName,
        String shippingAddress,
        String shippingCity,
        String shippingPostalCode,
        String paymentMethod,
        String paymentStatus,
        String cardLastFour,
        LocalDateTime createdAt,
        List<OrderItemResponse> items
) {}
