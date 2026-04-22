package com.uade.tpo.grupo7.marketplace.products.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO para asociar un valor de atributo a una variante")
public record VariantAttributeValueRequest(

    @Schema(description = "ID del valor de atributo", example = "1")
    @NotNull(message = "Attribute value id is required")
    Long attributeValueId
) {
}
