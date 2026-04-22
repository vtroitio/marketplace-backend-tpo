package com.uade.tpo.grupo7.marketplace.auth.service;

import org.springframework.stereotype.Component;

import com.uade.tpo.grupo7.marketplace.products.repository.ProductRepository;
import com.uade.tpo.grupo7.marketplace.products.repository.ReviewRepository;
import com.uade.tpo.grupo7.marketplace.users.entity.User;

import lombok.RequiredArgsConstructor;

@Component("ownership")
@RequiredArgsConstructor
public class OwnershipService {
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    public boolean isProductOwner(Long productId, User user) {       
        return productRepository.existsByIdAndSellerIdAndDeletedAtIsNull(
            productId,
            user.getId()
        );
    }

    public boolean isProductReviewOwner(Long reviewId, User user) {
        return reviewRepository.existsByIdAndBuyerIdAndDeletedAtIsNull(
            reviewId,
            user.getId()
        );
    }

}
