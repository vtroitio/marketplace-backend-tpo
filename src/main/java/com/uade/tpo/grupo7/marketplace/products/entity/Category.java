package com.uade.tpo.grupo7.marketplace.products.entity;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Schema(description = "Entidad que representa una categoria de productos")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador unico de la categoria", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Nombre de la categoria", example = "Remeras")
    private String name;

    @Column(nullable = false, unique = true)
    @Schema(description = "Codigo unico de la categoria", example = "REMERAS")
    private String code;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @Schema(description = "Categoria padre")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @Schema(description = "Subcategorias de la categoria")
    private Set<Category> children;
}
