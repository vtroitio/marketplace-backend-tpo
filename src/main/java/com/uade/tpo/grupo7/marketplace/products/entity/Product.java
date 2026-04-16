package com.uade.tpo.grupo7.marketplace.products.entity;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad que representa un producto en el marketplace")
public class Product {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Schema(description = "Identificador único del producto", example = "1")
    private Integer id;

    @Column(nullable = false)
    @Schema(description = "Nombre del producto", example = "Remera Negra")
    private String name;

    @Column(nullable = false)
    @Schema(description = "Precio del producto", example = "19.99")
    private Double price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Lista de imágenes asociadas al producto")
    private List<ProductImage> images;
}
