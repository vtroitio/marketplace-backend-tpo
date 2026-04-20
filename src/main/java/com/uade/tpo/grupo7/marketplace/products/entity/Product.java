package com.uade.tpo.grupo7.marketplace.products.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.uade.tpo.grupo7.marketplace.users.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador Ãºnico del producto", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Nombre del producto", example = "Remera Negra")
    private String name;

    @Column(nullable = false)
    @Schema(description = "Precio base del producto", example = "19.99")
    private Double price;

    @Column(nullable = false)
    @Schema(description = "DescripciÃ³n del producto", example = "Remera negra de algodÃ³n")
    private String description;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @Schema(description = "Usuario vendedor del producto")
    private User seller;

    @Column(nullable = false, updatable = false)
    @Schema(description = "Fecha de creaciÃ³n del producto")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha de borrado lÃ³gico del producto")
    private LocalDateTime deletedAt;

    @ManyToMany
    @JoinTable(
        name = "product_category",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @Schema(description = "CategorÃ­as asociadas al producto")
    private Set<Category> categories;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @Schema(description = "Variantes asociadas al producto")
    private List<ProductVariant> variants;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "Lista de imÃ¡genes asociadas al producto")
    private List<ProductImage> images;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }
    
    public boolean isActive() {
        return this.deletedAt == null;
    }

    public void softDelete() {
        if (this.deletedAt != null) return;
        this.deletedAt = LocalDateTime.now();
    }
}
