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
public class Review {
    private Integer id;
    private Integer productId;
    private Integer buyerId;
    private Integer rating;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
}
