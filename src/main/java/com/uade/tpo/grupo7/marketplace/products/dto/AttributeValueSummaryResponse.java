package com.uade.tpo.grupo7.marketplace.products.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de respuesta para un valor de atributo")
public record AttributeValueSummaryResponse(
    Long id,
    String value,
    String code,
    String hexColor,
    Long attributeId,
    String attributeName,
    String attributeCode
) {
}
