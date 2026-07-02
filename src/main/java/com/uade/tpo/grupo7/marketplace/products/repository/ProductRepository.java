package com.uade.tpo.grupo7.marketplace.products.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uade.tpo.grupo7.marketplace.products.dto.ProductResponse;
import com.uade.tpo.grupo7.marketplace.products.entity.Product;
import com.uade.tpo.grupo7.marketplace.users.entity.User;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByCategories_Id(Long categoryId);

    List<Product> findDistinctByCategories_IdIn(Collection<Long> categoryIds);

    boolean existsByIdAndSellerIdAndDeletedAtIsNull(Long id, Long sellerId);

    Optional<List<Product>> findBySeller(User seller);

    Page<Product> findBySellerIdAndDeletedAtIsNull(Pageable pageable, Long userId);

    @Query(value = """
                SELECT DISTINCT p
                FROM Product p
                WHERE p.active = true
                AND p.deletedAt IS NULL
                AND (:search IS NULL OR (
                    LOWER(p.name) LIKE CONCAT('%', LOWER(CAST(:search AS string)), '%')
                    OR LOWER(p.description) LIKE CONCAT('%', LOWER(CAST(:search AS string)), '%')
                ))
                AND (:categoryIds IS NULL OR EXISTS (
                    SELECT 1
                    FROM p.categories c
                    WHERE c.id IN :categoryIds
                ))
                AND (:minPrice IS NULL OR p.price >= :minPrice)
                AND (:maxPrice IS NULL OR p.price <= :maxPrice)
                AND (
                    (:colorIds IS NULL AND :sizeIds IS NULL)
                    OR EXISTS (
                        SELECT 1
                        FROM ProductVariant v
                        WHERE v.product = p
                        AND v.stock > 0
                        AND v.deletedAt IS NULL
                        AND (:colorIds IS NULL OR EXISTS (
                            SELECT 1
                            FROM VariantAttributeValue colorVav
                            WHERE colorVav.variant = v
                            AND colorVav.attributeValue.id IN :colorIds
                        ))
                        AND (:sizeIds IS NULL OR EXISTS (
                            SELECT 1
                            FROM VariantAttributeValue sizeVav
                            WHERE sizeVav.variant = v
                            AND sizeVav.attributeValue.id IN :sizeIds
                        ))
                    )
                )
            """, countQuery = """
                SELECT COUNT(DISTINCT p)
                FROM Product p
                WHERE p.active = true
                AND p.deletedAt IS NULL
                AND (:search IS NULL OR (
                    LOWER(p.name) LIKE CONCAT('%', LOWER(CAST(:search AS string)), '%')
                    OR LOWER(p.description) LIKE CONCAT('%', LOWER(CAST(:search AS string)), '%')
                ))
                AND (:categoryIds IS NULL OR EXISTS (
                    SELECT 1
                    FROM p.categories c
                    WHERE c.id IN :categoryIds
                ))
                AND (:minPrice IS NULL OR p.price >= :minPrice)
                AND (:maxPrice IS NULL OR p.price <= :maxPrice)
                AND (
                    (:colorIds IS NULL AND :sizeIds IS NULL)
                    OR EXISTS (
                        SELECT 1
                        FROM ProductVariant v
                        WHERE v.product = p
                        AND v.stock > 0
                        AND v.deletedAt IS NULL
                        AND (:colorIds IS NULL OR EXISTS (
                            SELECT 1
                            FROM VariantAttributeValue colorVav
                            WHERE colorVav.variant = v
                            AND colorVav.attributeValue.id IN :colorIds
                        ))
                        AND (:sizeIds IS NULL OR EXISTS (
                            SELECT 1
                            FROM VariantAttributeValue sizeVav
                            WHERE sizeVav.variant = v
                            AND sizeVav.attributeValue.id IN :sizeIds
                        ))
                    )
                )
            """)
    Page<Product> findWithFilters(
            @Param("search") String search,
            @Param("categoryIds") List<Long> categoryIds,
            @Param("colorIds") List<Long> colorIds,
            @Param("sizeIds") List<Long> sizeIds,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable);

    Page<Product> findByActiveTrueAndDeletedAtIsNull(Pageable pageable);
}