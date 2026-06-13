package com.uade.tpo.grupo7.marketplace.products.dto;

import java.util.List;

public record ProductDetailResponse(
    Long id,
    String name,
    String description,
    Double price,
    String coverImagePath,
    Integer totalStock,
    Boolean isActive,
    List<CategoryResponse> categories,
    List<ProductVariantResponse> variants
) {
}