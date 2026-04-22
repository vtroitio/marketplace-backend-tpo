package com.uade.tpo.grupo7.marketplace.cart.service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.uade.tpo.grupo7.marketplace.cart.dto.AddToCartRequest;
import com.uade.tpo.grupo7.marketplace.cart.dto.CartResponse;
import com.uade.tpo.grupo7.marketplace.cart.entity.Cart;
import com.uade.tpo.grupo7.marketplace.cart.entity.CartItem;

public interface CartService {

    CartResponse getCart(Long userId);

    CartResponse addItemToCart(Long userId, AddToCartRequest request);

    CartResponse updateItemQuantity(Long userId, Long itemId, int quantity);

    CartResponse removeItem(Long userId, Long itemId);

    void clearCart(Long userId);
}
