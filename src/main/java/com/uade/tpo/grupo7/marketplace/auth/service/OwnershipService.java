package com.uade.tpo.grupo7.marketplace.auth.service;

import org.springframework.stereotype.Component;

import com.uade.tpo.grupo7.marketplace.products.repository.ProductRepository;
import com.uade.tpo.grupo7.marketplace.users.entity.User;

import lombok.RequiredArgsConstructor;

@Component("ownership")
@RequiredArgsConstructor
public class OwnershipService {
    
    private final ProductRepository productRepository;

    public boolean isProductOwner(Long productId, User user) {       
        return productRepository.existsByIdAndSellerIdAndDeletedAtIsNull(
            productId,
            user.getId()
        );
    }

}
