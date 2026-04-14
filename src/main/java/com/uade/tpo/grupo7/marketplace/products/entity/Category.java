package com.uade.tpo.grupo7.marketplace.products.entity;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad que representa una categoría de productos")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único de la categoría", example = "1")
    private Integer id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Nombre de la categoría", example = "Remeras")
    private String name;

    @Column(nullable = false, unique = true)
    @Schema(description = "Código único de la categoría", example = "REMERAS")
    private String code;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @Schema(description = "Categoría padre")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private Set<Category> children;
}