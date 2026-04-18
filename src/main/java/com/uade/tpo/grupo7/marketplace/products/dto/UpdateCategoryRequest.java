package com.uade.tpo.grupo7.marketplace.products.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para la actualizacion parcial de una categoria")
public record UpdateCategoryRequest(

    @Schema(description = "Nombre de la categoria", example = "Remeras deportivas")
    String name,

    @Schema(description = "Codigo unico de la categoria", example = "REMERAS-DEP")
    String code,

    @Schema(description = "ID de la categoria padre. Usar null para dejarla como categoria raiz", example = "1", nullable = true)
    Long parentId
) {}
