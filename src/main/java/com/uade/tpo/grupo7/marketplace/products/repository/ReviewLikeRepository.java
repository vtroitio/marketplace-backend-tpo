package com.uade.tpo.grupo7.marketplace.products.repository;

import com.uade.tpo.grupo7.marketplace.products.entity.ReviewLike;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ReviewLikeRepository {

    private final List<ReviewLike> reviewLikes = new ArrayList<>();

    public List<ReviewLike> findAll() {
        return reviewLikes;
    }

    public Optional<ReviewLike> findById(Integer id) {
        return reviewLikes.stream()
                .filter(reviewLike -> reviewLike.getId().equals(id))
                .findFirst();
    }

    public List<ReviewLike> findByReviewId(Integer reviewId) {
        return reviewLikes.stream()
                .filter(reviewLike -> reviewLike.getReviewId().equals(reviewId))
                .toList();
    }

    public Optional<ReviewLike> findByReviewIdAndUserId(Integer reviewId, Integer userId) {
        return reviewLikes.stream()
                .filter(reviewLike ->
                        reviewLike.getReviewId().equals(reviewId)
                                && reviewLike.getBuyerId().equals(userId))
                .findFirst();
    }

    public ReviewLike create(ReviewLike reviewLike) {
        reviewLike.setId(reviewLikes.size() + 1);
        reviewLikes.add(reviewLike);
        return reviewLike;
    }

    public void delete(ReviewLike reviewLike) {
        reviewLikes.remove(reviewLike);
    }

    public long countByReviewId(Integer reviewId) {
        return reviewLikes.stream()
                .filter(reviewLike -> reviewLike.getReviewId().equals(reviewId))
                .count();
    }
}