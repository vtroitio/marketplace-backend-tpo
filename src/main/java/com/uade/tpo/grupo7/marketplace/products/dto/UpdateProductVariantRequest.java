package com.uade.tpo.grupo7.marketplace.products.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Schema(description = "DTO para la actualizacion de una variante de producto")
public record UpdateProductVariantRequest(

    @Schema(description = "ID de la variante si ya existe", example = "1")
    Integer id,

    @Schema(description = "SKU unico de la variante", example = "REM-NAR-NEG-S")
    @NotBlank(message = "Variant SKU is required")
    String sku,

    @Schema(description = "Precio de la variante", example = "19999.0")
    @Positive(message = "Variant price must be positive")
    Double price,

    @Schema(description = "Stock disponible de la variante", example = "10")
    @PositiveOrZero(message = "Variant stock must be positive or zero")
    Integer stock,

    @Schema(description = "Valores de atributos asociados a la variante")
    List<@Valid VariantAttributeValueRequest> attributeValues
) {
}
