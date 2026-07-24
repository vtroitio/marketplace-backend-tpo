package com.uade.tpo.grupo7.marketplace.order.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uade.tpo.grupo7.marketplace.order.entity.PurchaseOrder;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    List<PurchaseOrder> findByBuyerIdOrderByCreatedAtDesc(Long buyerId);

    Optional<PurchaseOrder> findByIdAndBuyerId(Long id, Long buyerId);

    @Query("""
            SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END
            FROM PurchaseOrder o
            JOIN o.items i
            WHERE o.buyer.id = :buyerId
            AND i.productVariant.product.id = :productId
            """)
    boolean existsByBuyerIdAndProductId(
            @Param("buyerId") Long buyerId,
            @Param("productId") Long productId);
}
