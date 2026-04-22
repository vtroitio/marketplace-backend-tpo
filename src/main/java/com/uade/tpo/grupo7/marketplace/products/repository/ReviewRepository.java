package com.uade.tpo.grupo7.marketplace.products.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.grupo7.marketplace.products.entity.Product;
import com.uade.tpo.grupo7.marketplace.products.entity.Review;
import com.uade.tpo.grupo7.marketplace.users.entity.User;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProduct_Id(Long productId);

    List<Review> findAllByProduct_IdAndDeletedAtIsNull(Long productId);

    boolean existsByProductAndBuyerAndDeletedAtIsNull(Product product, User buyer);

    boolean existsByIdAndBuyerIdAndDeletedAtIsNull(Long reviewId, Long id);
}