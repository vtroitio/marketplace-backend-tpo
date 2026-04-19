package com.uade.tpo.grupo7.marketplace.products.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateReviewLikeRequest {

    @NotNull(message = "buyerId is required")
    private Integer buyerId;
}