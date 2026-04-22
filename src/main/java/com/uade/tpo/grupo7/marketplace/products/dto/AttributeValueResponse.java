package com.uade.tpo.grupo7.marketplace.products.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de respuesta para los valores de un atributo")
public record AttributeValueResponse(
    Long id,
    String value,
    String code,
    String hexColor
) {}
