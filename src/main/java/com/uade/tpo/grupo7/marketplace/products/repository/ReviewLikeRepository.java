package com.uade.tpo.grupo7.marketplace.products.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.grupo7.marketplace.products.entity.ReviewLike;
import com.uade.tpo.grupo7.marketplace.users.entity.User;

@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    Optional<ReviewLike> findByReviewIdAndBuyer(Long reviewId, User buyer);

    long countByReviewId(Long reviewId);

    boolean existsByReviewIdAndBuyer(Long reviewId, User buyer);
}