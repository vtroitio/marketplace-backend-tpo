package com.uade.tpo.grupo7.marketplace.products.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.grupo7.marketplace.products.entity.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {

    int countByProductId(int productId);

    List<ProductImage> findAllByProductId(int productId);

}
