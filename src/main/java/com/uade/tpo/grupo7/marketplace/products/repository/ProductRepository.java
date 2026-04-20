package com.uade.tpo.grupo7.marketplace.products.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.grupo7.marketplace.products.entity.Product;
import com.uade.tpo.grupo7.marketplace.users.entity.User;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByCategories_Id(Long categoryId);
    List<Product> findDistinctByCategories_IdIn(Collection<Long> categoryIds);
    boolean existsByIdAndSellerIdAndDeletedAtIsNull(Long productId, Long sellerId);
    Optional<List<Product>> findBySeller(User seller);
}
