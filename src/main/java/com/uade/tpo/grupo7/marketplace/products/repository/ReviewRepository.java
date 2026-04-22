package com.uade.tpo.grupo7.marketplace.products.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.grupo7.marketplace.products.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProduct_Id(Long productId);

    List<Review> findAllByDeletedAtIsNull(Long productId);

    boolean existsByIdAndBuyerIdAndDeletedAtIsNull(Long reviewId, Long id);
}