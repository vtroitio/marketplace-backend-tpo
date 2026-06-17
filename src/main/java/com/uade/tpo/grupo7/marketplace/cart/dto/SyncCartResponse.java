package com.uade.tpo.grupo7.marketplace.cart.dto;

import java.util.List;

public record SyncCartResponse(
    CartResponse cart,
    List<SyncCartItemResultResponse> items
) {}
