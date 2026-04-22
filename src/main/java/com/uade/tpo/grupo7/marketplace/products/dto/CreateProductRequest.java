package com.uade.tpo.grupo7.marketplace.products.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "DTO para la creacion de un nuevo producto en el marketplace")
public record CreateProductRequest(

    @Schema(description = "Nombre del producto", example = "Remera Negra")
    @NotBlank(message = "Name is required")
    String name,

    @Schema(description = "Precio del producto", example = "19.99")
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive and not 0")
    Double price,

    @Schema(description = "Descripcion del producto", example = "Remera negra de algodon")
    @NotBlank(message = "Description is required")
    String description,

    @Schema(description = "Lista de IDs de categorias asociadas", example = "[1, 2]")
    List<Long> categoryIds,

    @Schema(description = "Lista de variantes asociadas al producto")
    List<@Valid CreateProductVariantRequest> variants
) {
}
