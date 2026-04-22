package com.uade.tpo.grupo7.marketplace.products.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de respuesta para variantes de producto")
public record ProductVariantResponse(
    Integer id,
    String sku,
    Double price,
    Integer stock,
    List<AttributeValueSummaryResponse> attributeValues
) {
}
