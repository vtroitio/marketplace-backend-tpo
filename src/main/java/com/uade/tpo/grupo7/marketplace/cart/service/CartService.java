package com.uade.tpo.grupo7.marketplace.cart.service;

import com.uade.tpo.grupo7.marketplace.cart.dto.AddToCartRequest;
import com.uade.tpo.grupo7.marketplace.cart.dto.CartResponse;

public interface CartService {

    CartResponse getCart(Long userId);

    CartResponse addItemToCart(Long userId, AddToCartRequest request);

    CartResponse updateItemQuantity(Long userId, Long itemId, int quantity);

    CartResponse removeItem(Long userId, Long itemId);

    void clearCart(Long userId);
}
