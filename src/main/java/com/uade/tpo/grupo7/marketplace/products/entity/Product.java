package com.uade.tpo.grupo7.marketplace.products.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Entidad que representa un producto en el marketplace")
public class Product {

    @Schema(description = "Identificador único del producto", example = "1")
    private Integer id;

    @Schema(description = "Nombre del producto", example = "Remera Negra")
    private String name;

    @Schema(description = "Precio del producto", example = "19.99")
    private Double price;
}
