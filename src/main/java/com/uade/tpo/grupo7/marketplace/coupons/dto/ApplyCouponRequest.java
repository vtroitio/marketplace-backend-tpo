package com.uade.tpo.grupo7.marketplace.coupons.dto;

import jakarta.validation.constraints.NotBlank;

public record ApplyCouponRequest(
    @NotBlank(message = "El código del cupón es obligatorio")
    String code
) {}
