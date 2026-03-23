package com.uade.tpo.grupo7.marketplace.products.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

@Schema(description = "DTO para la actualización del detalle de un producto en el marketplace")
public record UpdateProductRequest(

    @Schema(description = "Nombre del producto", example = "Remera Negra")
    String name,

    @Schema(description = "Precio del producto", example = "19.99")
    @Positive(message = "Price must be positive and not 0")
    Double price
) {} 
