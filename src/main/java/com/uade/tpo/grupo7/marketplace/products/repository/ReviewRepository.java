package com.uade.tpo.grupo7.marketplace.products.repository;

import com.uade.tpo.grupo7.marketplace.products.entity.Review;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ReviewRepository {

    private final List<Review> reviews = new ArrayList<>();

    public List<Review> findAll() {
        return reviews;
    }

    public Optional<Review> findById(Integer id) {
        return reviews.stream()
                .filter(review -> review.getId().equals(id))
                .findFirst();
    }

    public List<Review> findByProductId(Integer productId) {
        return reviews.stream()
                .filter(review -> review.getProductId().equals(productId))
                .toList();
    }

    public Review create(Review review) {
        review.setId(reviews.size() + 1);
        reviews.add(review);
        return review;
    }
}