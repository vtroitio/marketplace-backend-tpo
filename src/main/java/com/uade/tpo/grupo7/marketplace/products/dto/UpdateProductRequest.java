package com.uade.tpo.grupo7.marketplace.products.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

@Schema(description = "DTO para la actualizacion del detalle de un producto en el marketplace")
public record UpdateProductRequest(

    @Schema(description = "Nombre del producto", example = "Remera Negra")
    String name,

    @Schema(description = "Precio del producto", example = "19.99")
    @Positive(message = "Price must be positive and not 0")
    Double price,

    @Schema(description = "Descripcion del producto", example = "Remera negra de algodon")
    String description,

    @Schema(description = "Lista de IDs de categorias asociadas", example = "[1, 2]")
    List<Long> categoryIds,

    @Schema(description = "Lista de variantes asociadas al producto")
    List<@Valid UpdateProductVariantRequest> variants
) {
}
