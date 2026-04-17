package com.uade.tpo.grupo7.marketplace.products.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de respuesta para productos")
public record ProductResponse(
    Long id,
    String name,
    Double price,
    String description,
    List<CategoryResponse> categories
) {}
