package com.uade.tpo.grupo7.marketplace.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SyncCartItemRequest(
    @NotNull(message = "La variante es obligatoria")
    Integer variantId,

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a cero")
    Integer quantity
) {}
