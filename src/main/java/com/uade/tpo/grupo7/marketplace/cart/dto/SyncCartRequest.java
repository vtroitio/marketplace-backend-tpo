package com.uade.tpo.grupo7.marketplace.cart.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record SyncCartRequest(
    @NotEmpty(message = "El carrito local no puede estar vacío")
    List<@Valid SyncCartItemRequest> items
) {}
