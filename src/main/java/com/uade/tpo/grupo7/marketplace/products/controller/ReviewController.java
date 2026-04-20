package com.uade.tpo.grupo7.marketplace.products.controller;

import com.uade.tpo.grupo7.marketplace.products.dto.CreateReviewRequest;
import com.uade.tpo.grupo7.marketplace.products.entity.Review;
import com.uade.tpo.grupo7.marketplace.products.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // @PostMapping("/{productId}/reviews")
    // @ResponseStatus(HttpStatus.CREATED)
    // public Review createReview(
    //         @PathVariable Integer productId,
    //         @RequestBody @Valid CreateReviewRequest request
    // ) {
    //     return reviewService.createReview(productId, request);
    // }

    // @GetMapping("/{productId}/reviews")
    // public List<Review> getReviewsByProductId(@PathVariable Integer productId) {
    //     return reviewService.getReviewsByProductId(productId);
    // }
}