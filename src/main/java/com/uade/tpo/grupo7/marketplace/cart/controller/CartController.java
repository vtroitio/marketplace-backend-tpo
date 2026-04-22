package com.uade.tpo.grupo7.marketplace.cart.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.uade.tpo.grupo7.marketplace.cart.dto.AddToCartRequest;
import com.uade.tpo.grupo7.marketplace.cart.dto.CartResponse;
import com.uade.tpo.grupo7.marketplace.cart.dto.UpdateCartItemRequest;
import com.uade.tpo.grupo7.marketplace.cart.service.CartService;
import com.uade.tpo.grupo7.marketplace.users.entity.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("cart")
@PreAuthorize("hasRole('BUYER')")
@Tag(name = "Cart", description = "Endpoints del carrito del usuario autenticado")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    @Operation(summary = "Obtener el carrito del usuario autenticado")
    public CartResponse getCart(@AuthenticationPrincipal Optional<User> principal) {
        return cartService.getCart(currentUserId(principal));
    }

    @PostMapping("items")
    @Operation(summary = "Agregar un item al carrito")
    public CartResponse addItem(
            @AuthenticationPrincipal Optional<User> principal,
            @Valid @RequestBody AddToCartRequest request
    ) {
        return cartService.addItemToCart(currentUserId(principal), request);
    }

    @PatchMapping("items/{itemId}")
    @Operation(summary = "Actualizar la cantidad de un item del carrito")
    public CartResponse updateItem(
            @AuthenticationPrincipal Optional<User> principal,
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateCartItemRequest request
    ) {
        return cartService.updateItemQuantity(currentUserId(principal), itemId, request.quantity());
    }

    @DeleteMapping("items/{itemId}")
    @Operation(summary = "Eliminar un item del carrito")
    public CartResponse removeItem(
            @AuthenticationPrincipal Optional<User> principal,
            @PathVariable Long itemId
    ) {
        return cartService.removeItem(currentUserId(principal), itemId);
    }

    @DeleteMapping
    @Operation(summary = "Vaciar el carrito")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal Optional<User> principal) {
        cartService.clearCart(currentUserId(principal));
        return ResponseEntity.noContent().build();
    }

    private Long currentUserId(Optional<User> principal) {
        return principal
                .map(User::getId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required"));
    }
}
