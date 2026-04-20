package com.uade.tpo.grupo7.marketplace.products.service;

import com.uade.tpo.grupo7.marketplace.products.dto.CreateReviewRequest;
import com.uade.tpo.grupo7.marketplace.products.entity.Product;
import com.uade.tpo.grupo7.marketplace.products.entity.Review;
import com.uade.tpo.grupo7.marketplace.products.mapper.ReviewMapper;
import com.uade.tpo.grupo7.marketplace.products.repository.ProductRepository;
import com.uade.tpo.grupo7.marketplace.products.repository.ReviewRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
    }

    // public Review createReview(Integer productId, CreateReviewRequest request) {
        // Product product = productRepository.findById(productId)
                // .orElseThrow(() -> new ResponseStatusException(
                        // HttpStatus.NOT_FOUND,
                        // "Product with id " + productId + " not found"
                // ));

        // Review review = ReviewMapper.toEntity(request, product.getId());
        // return reviewRepository.create(review);
    // }

    // public List<Review> getReviewsByProductId(Integer productId) {
        // productRepository.findById(productId)
                // .orElseThrow(() -> new ResponseStatusException(
                        // HttpStatus.NOT_FOUND,
                        // "Product with id " + productId + " not found"
                // ));

        // return reviewRepository.findByProductId(productId);
    // }
}