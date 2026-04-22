package com.uade.tpo.grupo7.marketplace.products.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record UpdateReviewRequest(

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 10, message = "Rating must be at most 10")
    Integer rating,

    String title,

    String description
) {}