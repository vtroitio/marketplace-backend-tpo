package com.uade.tpo.grupo7.marketplace.cart.service;

import com.uade.tpo.grupo7.marketplace.cart.dto.AddToCartRequest;
import com.uade.tpo.grupo7.marketplace.cart.dto.CartResponse;
import com.uade.tpo.grupo7.marketplace.cart.dto.CartValidationResponse;
import com.uade.tpo.grupo7.marketplace.cart.dto.SyncCartRequest;
import com.uade.tpo.grupo7.marketplace.cart.dto.SyncCartResponse;

public interface CartService {

    CartResponse getCart(Long userId);

    CartResponse addItemToCart(Long userId, AddToCartRequest request);

    CartResponse updateItemQuantity(Long userId, Long itemId, int quantity);

    CartResponse removeItem(Long userId, Long itemId);

    SyncCartResponse syncCart(Long userId, SyncCartRequest request);

    void clearCart(Long userId);

    CartValidationResponse validateCartForCheckout(Long userId);
}
