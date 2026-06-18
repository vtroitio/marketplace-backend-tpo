package com.uade.tpo.grupo7.marketplace.coupons.dto;

import java.math.BigDecimal;

public record CouponValidationResponse(
    String code,
    BigDecimal subtotal,
    BigDecimal discountAmount,
    BigDecimal totalAfterDiscount,
    String message
) {}
