package com.uade.tpo.grupo7.marketplace.products.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.grupo7.marketplace.products.entity.ReviewLike;

@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    Optional<ReviewLike> findByReview_IdAndBuyerId(Long reviewId, Integer buyerId);

    long countByReview_Id(Long reviewId);
}