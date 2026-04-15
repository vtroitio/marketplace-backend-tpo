package com.uade.tpo.grupo7.marketplace.products.entity;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JsonIgnore
    @Schema(description = "Categoría padre")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    private Set<Category> children;
}