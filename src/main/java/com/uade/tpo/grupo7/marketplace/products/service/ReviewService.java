package com.uade.tpo.grupo7.marketplace.products.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.uade.tpo.grupo7.marketplace.products.dto.CreateReviewRequest;
import com.uade.tpo.grupo7.marketplace.products.dto.UpdateReviewRequest;
import com.uade.tpo.grupo7.marketplace.products.entity.Product;
import com.uade.tpo.grupo7.marketplace.products.entity.Review;
import com.uade.tpo.grupo7.marketplace.products.mapper.ReviewMapper;
import com.uade.tpo.grupo7.marketplace.products.repository.ProductRepository;
import com.uade.tpo.grupo7.marketplace.products.repository.ReviewRepository;
import com.uade.tpo.grupo7.marketplace.users.entity.User;
import com.uade.tpo.grupo7.marketplace.users.repository.UserRepository;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
    }

    public Review createReview(Long productId, CreateReviewRequest request, User user) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Product with id " + productId + " not found"
        ));

        Review review = ReviewMapper.toEntity(request, product, user);
        return reviewRepository.save(review);
    }

    public List<Review> getReviewsByProductId(Long productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Product with id " + productId + " not found"
        ));

        return reviewRepository.findByProduct_Id(productId);
    }

    public Review updateReview(Long reviewId, UpdateReviewRequest request, User user) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review no encontrada"));

        if (!review.getBuyer().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tenés permiso para modificar esta review");
        }

        if (request.title() != null) {
            review.setTitle(request.title());
        }
        if (request.description() != null) {
            review.setDescription(request.description());
        }
        if (request.rating() != null && request.rating() >= 1 && request.rating() <= 10) {
            review.setRating(request.rating());
        }
        
        return reviewRepository.save(review);
    }
}
