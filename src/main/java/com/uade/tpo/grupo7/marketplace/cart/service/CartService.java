package com.uade.tpo.grupo7.marketplace.cart.service;

import com.uade.tpo.grupo7.marketplace.cart.dto.AddToCartRequest;
import com.uade.tpo.grupo7.marketplace.cart.dto.CartItemResponse;
import com.uade.tpo.grupo7.marketplace.cart.dto.CartResponse;
import com.uade.tpo.grupo7.marketplace.cart.entity.Cart;
import com.uade.tpo.grupo7.marketplace.cart.entity.CartItem;
import com.uade.tpo.grupo7.marketplace.cart.repository.CartItemRepository;
import com.uade.tpo.grupo7.marketplace.cart.repository.CartRepository;
import com.uade.tpo.grupo7.marketplace.products.entity.ProductVariant;
import com.uade.tpo.grupo7.marketplace.products.repository.ProductVariantRepository;
import com.uade.tpo.grupo7.marketplace.users.entity.User;
import com.uade.tpo.grupo7.marketplace.users.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductVariantRepository productVariantRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       ProductVariantRepository productVariantRepository,
                       UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productVariantRepository = productVariantRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CartResponse addItemToCart(Long userId, AddToCartRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Cart cart = cartRepository.findByBuyerId(userId)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .buyer(user)
                            .totalAmount(BigDecimal.ZERO)
                            .build();
                    return cartRepository.save(newCart);
                });

        // ADAPTACIÓN 1: Convertimos tu Long a Integer (intValue) para poder buscar su variante
        ProductVariant variant = productVariantRepository.findById(request.getProductVariantId().intValue())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductVariant().getId().equals(variant.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .productVariant(variant)
                    .quantity(request.getQuantity())
                    .build();
            cart.getItems().add(newItem);
        }

        recalculateTotal(cart);
        Cart savedCart = cartRepository.save(cart);

        return mapToResponse(savedCart);
    }

    private void recalculateTotal(Cart cart) {
        BigDecimal total = cart.getItems().stream()
                // ADAPTACIÓN 2: Convertimos su precio Double a BigDecimal (valueOf) para hacer nuestros cálculos
                .map(item -> BigDecimal.valueOf(item.getProductVariant().getPrice())
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalAmount(total);
    }

    private CartResponse mapToResponse(Cart cart) {
        return CartResponse.builder()
                .cartId(cart.getId())
                .totalAmount(cart.getTotalAmount())
                .items(cart.getItems().stream().map(item -> 
                    CartItemResponse.builder()
                        .id(item.getId())
                        // ADAPTACIÓN 3: Convertimos su Integer a Long para enviarlo en nuestro DTO
                        .productVariantId(item.getProductVariant().getId().longValue())
                        // ADAPTACIÓN 4: Buscamos el nombre "haciendo puente" a través del objeto Product
                        .productName(item.getProductVariant().getProduct().getName()) 
                        .quantity(item.getQuantity())
                        // ADAPTACIÓN 5: Convertimos de nuevo su Double a BigDecimal para el frontend
                        .unitPrice(BigDecimal.valueOf(item.getProductVariant().getPrice()))
                        .subtotal(BigDecimal.valueOf(item.getProductVariant().getPrice()).multiply(new BigDecimal(item.getQuantity())))
                        .build()
                ).collect(Collectors.toList()))
                .build();
    }
}