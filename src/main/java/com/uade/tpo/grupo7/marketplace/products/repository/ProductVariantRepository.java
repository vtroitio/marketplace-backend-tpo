package com.uade.tpo.grupo7.marketplace.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.uade.tpo.grupo7.marketplace.products.entity.ProductVariant;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {
}