package com.uade.tpo.grupo7.marketplace.cart.dto;

import java.util.List;

public record CartValidationResponse(
    boolean valid,
    CartResponse cart,
    List<CartValidationIssueResponse> issues
) {
}
