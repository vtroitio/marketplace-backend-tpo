package com.uade.tpo.grupo7.marketplace.order.dto;

import jakarta.validation.constraints.NotBlank;

public record CheckoutRequest(
    @NotBlank(message = "El nombre completo es obligatorio")
    String fullName,

    @NotBlank(message = "La dirección de envío es obligatoria")
    String address,

    @NotBlank(message = "La ciudad es obligatoria")
    String city,

    @NotBlank(message = "El código postal es obligatorio")
    String postalCode,

    String couponCode,

    String cardNumber,

    String cardExpiration,

    String cardCvv
) {}
