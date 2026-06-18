package com.uade.tpo.grupo7.marketplace.coupons.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.uade.tpo.grupo7.marketplace.common.enums.CouponDiscountType;
import com.uade.tpo.grupo7.marketplace.coupons.dto.CouponValidationResponse;
import com.uade.tpo.grupo7.marketplace.coupons.entity.Coupon;
import com.uade.tpo.grupo7.marketplace.coupons.repository.CouponRepository;

@Service
public class CouponService {

    private static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100);

    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public CouponValidationResponse validateCoupon(String code, BigDecimal subtotal) {
        Coupon coupon = getValidCoupon(code, subtotal);

        BigDecimal discountAmount = calculateDiscount(coupon, subtotal);
        BigDecimal totalAfterDiscount = subtotal.subtract(discountAmount);

        return new CouponValidationResponse(
                coupon.getCode(),
                subtotal,
                discountAmount,
                totalAfterDiscount,
                "Cupón aplicado correctamente"
        );
    }

    public BigDecimal calculateDiscountForCheckout(String code, BigDecimal subtotal) {
        if (code == null || code.isBlank()) {
            return BigDecimal.ZERO;
        }

        Coupon coupon = getValidCoupon(code, subtotal);

        return calculateDiscount(coupon, subtotal);
    }

    private Coupon getValidCoupon(String code, BigDecimal subtotal) {
        String normalizedCode = code.trim().toUpperCase();

        Coupon coupon = couponRepository.findByCodeIgnoreCase(normalizedCode)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "El cupón no existe"
                ));

        if (!coupon.isActive()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El cupón no está activo o ya venció"
            );
        }

        BigDecimal minimumSubtotal = coupon.getMinimumSubtotal();

        if (minimumSubtotal != null && subtotal.compareTo(minimumSubtotal) < 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El subtotal no alcanza el mínimo requerido para este cupón"
            );
        }

        return coupon;
    }

    private BigDecimal calculateDiscount(Coupon coupon, BigDecimal subtotal) {
        BigDecimal discount;

        if (coupon.getDiscountType() == CouponDiscountType.PERCENTAGE) {
            discount = subtotal
                    .multiply(coupon.getDiscountValue())
                    .divide(ONE_HUNDRED, 2, RoundingMode.HALF_UP);
        } else {
            discount = coupon.getDiscountValue();
        }

        if (discount.compareTo(subtotal) > 0) {
            discount = subtotal;
        }

        return discount.setScale(2, RoundingMode.HALF_UP);
    }
}