package com.uade.tpo.grupo7.marketplace.order.entity;

import com.uade.tpo.grupo7.marketplace.users.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @ManyToOne: Muchas órdenes pertenecen a un solo User (comprador)
    // FetchType.LAZY: Optimiza la memoria, no carga todo el perfil del usuario hasta que lo pidas
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", referencedColumnName = "id", nullable = false)
    private User buyer; 

    // nullable = false: El estado de la orden no puede quedar vacío
    @Column(nullable = false)
    private String status;

    // BigDecimal: El tipo de dato más preciso en Java para manejar dinero
    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    // @CreatedDate: Spring Boot anota la fecha automáticamente cuando se crea
    // updatable = false: Evita que alguien modifique la fecha de creación después
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // @OneToMany: Una orden contiene una lista con muchos items
    // mappedBy: Le dice a Spring que busque la variable "purchaseOrder" en la clase OrderItem
    // cascade = CascadeType.ALL: Si guardo la orden, se guardan los items automáticamente
    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>(); // Se inicializa vacía para evitar errores NullPointerException
}