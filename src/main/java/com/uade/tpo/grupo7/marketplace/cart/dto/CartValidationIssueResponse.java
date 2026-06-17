package com.uade.tpo.grupo7.marketplace.cart.dto;

public record CartValidationIssueResponse(
    Integer productVariantId,
    String status,
    String message
) {}
