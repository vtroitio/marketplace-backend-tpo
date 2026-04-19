package com.uade.tpo.grupo7.marketplace.products.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.grupo7.marketplace.products.dto.CreateReviewRequest;
import com.uade.tpo.grupo7.marketplace.products.entity.Review;
import com.uade.tpo.grupo7.marketplace.products.service.ReviewService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{productId}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public Review createReview(
            @PathVariable Long productId,
            @RequestBody @Valid CreateReviewRequest request
    ) {
        return reviewService.createReview(productId, request);
    }

    @GetMapping("/{productId}/reviews")
    public List<Review> getReviewsByProductId(@PathVariable Long productId) {
        return reviewService.getReviewsByProductId(productId);
    }
}