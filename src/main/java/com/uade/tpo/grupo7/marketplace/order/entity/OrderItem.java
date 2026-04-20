package com.uade.tpo.grupo7.marketplace.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;

import com.uade.tpo.grupo7.marketplace.products.entity.ProductVariant;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ORDER_ITEM")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @ManyToOne: Muchos items pueden pertenecer a una misma Orden
    // Esta es la variable a la que hace referencia el "mappedBy" de PurchaseOrder
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private PurchaseOrder purchaseOrder;

    // @ManyToOne: Muchos items pueden ser copias de una misma variante de producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id", nullable = false)
    private ProductVariant productVariant;

    // Cantidad del producto comprado
    @Column(nullable = false)
    private Integer quantity;

    // Precio unitario al momento de la compra (histórico)
    @Column(name = "unit_price", nullable = false)
    private BigDecimal unitPrice;

    // Total de esta línea (quantity * unitPrice)
    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;
}
