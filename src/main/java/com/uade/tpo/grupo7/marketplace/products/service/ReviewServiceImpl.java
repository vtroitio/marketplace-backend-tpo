package com.uade.tpo.grupo7.marketplace.products.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.uade.tpo.grupo7.marketplace.products.dto.CreateReviewRequest;
import com.uade.tpo.grupo7.marketplace.products.dto.ReviewResponse;
import com.uade.tpo.grupo7.marketplace.products.dto.UpdateReviewRequest;
import com.uade.tpo.grupo7.marketplace.products.entity.Product;
import com.uade.tpo.grupo7.marketplace.products.entity.Review;
import com.uade.tpo.grupo7.marketplace.products.mapper.ReviewMapper;
import com.uade.tpo.grupo7.marketplace.products.repository.ProductRepository;
import com.uade.tpo.grupo7.marketplace.products.repository.ReviewRepository;
import com.uade.tpo.grupo7.marketplace.users.entity.User;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
    }

    @Override
    public ReviewResponse createReview(Long productId, CreateReviewRequest request, User user) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Product with id " + productId + " not found"
        ));

        if (reviewRepository.existsByProductAndBuyerAndDeletedAtIsNull(product, user)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ya has dejado una review para este producto."
            );
        }
        Review review = ReviewMapper.toEntity(request, product, user);
        return ReviewMapper.toResponse(reviewRepository.save(review));
    }

    @Override
    public List<ReviewResponse> getReviewsByProductId(Long productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Product with id " + productId + " not found"
        ));

        return reviewRepository.findAllByDeletedAtIsNull(productId).stream()
                .map(ReviewMapper::toResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public ReviewResponse updateReview(Long reviewId, UpdateReviewRequest request, User user) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));

        if (request.title() != null) {
            review.setTitle(request.title());
        }
        if (request.description() != null) {
            review.setDescription(request.description());
        }
        if (request.rating() != null) {
            review.setRating(request.rating());
        }

        return ReviewMapper.toResponse(reviewRepository.save(review));
    }

    @Override
    public void deleteReview(Long reviewId, User user) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
        reviewRepository.delete(review);
    }
}