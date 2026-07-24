package com.uade.tpo.grupo7.marketplace.cart.dto;

public record SyncCartItemResultResponse(
    Integer variantId,
    Integer requestedQuantity,
    Integer addedQuantity,
    String status,
    String message
) {}
