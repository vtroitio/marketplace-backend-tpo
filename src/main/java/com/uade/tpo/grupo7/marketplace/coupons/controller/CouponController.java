package com.uade.tpo.grupo7.marketplace.coupons.controller;

import java.math.BigDecimal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.uade.tpo.grupo7.marketplace.cart.entity.Cart;
import com.uade.tpo.grupo7.marketplace.cart.repository.CartRepository;
import com.uade.tpo.grupo7.marketplace.coupons.dto.ApplyCouponRequest;
import com.uade.tpo.grupo7.marketplace.coupons.dto.CouponValidationResponse;
import com.uade.tpo.grupo7.marketplace.coupons.service.CouponService;
import com.uade.tpo.grupo7.marketplace.users.entity.User;

import jakarta.validation.Valid;

@RestController
@RequestMapping("coupons")
public class CouponController {

    private final CouponService couponService;
    private final CartRepository cartRepository;

    public CouponController(CouponService couponService, CartRepository cartRepository) {
        this.couponService = couponService;
        this.cartRepository = cartRepository;
    }

    @PostMapping("validate")
    @PreAuthorize("hasRole('BUYER')")
    public CouponValidationResponse validateCoupon(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody ApplyCouponRequest request
    ) {
        Cart cart = cartRepository.findByBuyerId(user.getId())
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        BigDecimal subtotal = cart.getTotalAmount();

        return couponService.validateCoupon(request.code(), subtotal);
    }
}