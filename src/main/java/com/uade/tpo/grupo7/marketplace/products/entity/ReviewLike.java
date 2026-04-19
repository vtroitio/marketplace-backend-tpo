package com.uade.tpo.grupo7.marketplace.products.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewLike {
    private Integer id;
    private Integer reviewId;
    private Integer BuyerId;
    private LocalDateTime createdAt;
}