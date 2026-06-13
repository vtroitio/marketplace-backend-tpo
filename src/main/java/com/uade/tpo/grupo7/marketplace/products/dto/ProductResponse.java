package com.uade.tpo.grupo7.marketplace.products.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de respuesta para productos")
public record ProductResponse(
    Long id,
    String name,
    Double price,
    // String description,
    String coverImagePath,
    Integer totalStock,
    Integer totalVariants,
    boolean isActive
    // List<CategoryResponse> categories,
    // List<ProductVariantResponse> variants
) {
}
