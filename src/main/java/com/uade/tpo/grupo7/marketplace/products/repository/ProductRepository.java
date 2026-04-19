package com.uade.tpo.grupo7.marketplace.products.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.grupo7.marketplace.products.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByCategories_Id(Long categoryId);
    List<Product> findDistinctByCategories_IdIn(Collection<Long> categoryIds);
    boolean existsByIdAndSellerIdAndDeletedAtIsNull(Long productId, Long sellerId);
}
