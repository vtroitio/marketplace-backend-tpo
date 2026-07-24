package com.uade.tpo.grupo7.marketplace.products.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.grupo7.marketplace.products.entity.ProductImage;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    int countByVariantId(Integer variantId);

    List<ProductImage> findAllByVariantIdOrderByPositionAsc(Integer variantId);

    List<ProductImage> findAllByVariantProductIdOrderByPositionAsc(Long productId);

}
