package com.uade.tpo.grupo7.marketplace.products.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO para la creacion de una nueva categoria")
public record CreateCategoryRequest(

    @Schema(description = "Nombre de la categoria", example = "Remeras")
    @NotBlank(message = "Name is required")
    String name,

    @Schema(description = "Codigo unico de la categoria", example = "REMERAS")
    @NotBlank(message = "Code is required")
    String code,

    @Schema(description = "ID de la categoria padre. Usar null para una categoria raiz", example = "1", nullable = true)
    Long parentId
) {}
