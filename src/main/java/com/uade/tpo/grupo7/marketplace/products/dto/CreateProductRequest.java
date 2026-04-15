package com.uade.tpo.grupo7.marketplace.products.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "DTO para la creación de un nuevo producto en el marketplace")
public record CreateProductRequest(

    @Schema(description = "Nombre del producto", example = "Remera Negra")
    @NotBlank(message = "Name is required")
    String name,

    @Schema(description = "Precio del producto", example = "19.99")
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive and not 0")
    Double price,

    @Schema(description = "Descripción del producto", example = "Remera negra de algodón")
    @NotBlank(message = "Description is required")
    String description
) {}