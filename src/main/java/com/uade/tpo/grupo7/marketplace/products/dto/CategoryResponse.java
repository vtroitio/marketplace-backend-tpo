package com.uade.tpo.grupo7.marketplace.products.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de respuesta para categorias")
public record CategoryResponse(
    Long id,
    String name,
    String code,
    Long parentId
) {}
