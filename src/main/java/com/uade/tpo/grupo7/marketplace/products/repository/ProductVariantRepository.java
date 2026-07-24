package com.uade.tpo.grupo7.marketplace.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.uade.tpo.grupo7.marketplace.products.entity.ProductVariant;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {
    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(String sku, Integer id);

    @Modifying(flushAutomatically = true)
    @Query("""
                UPDATE ProductVariant v
                SET v.stock = v.stock - :quantity
                WHERE v.id = :variantId
                  AND v.stock IS NOT NULL
                  AND v.stock >= :quantity
            """)
    int decrementStockIfEnough(
            @Param("variantId") int variantId,
            @Param("quantity") int quantity);
}
