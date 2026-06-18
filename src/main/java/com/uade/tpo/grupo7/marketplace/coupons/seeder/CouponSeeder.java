package com.uade.tpo.grupo7.marketplace.coupons.seeder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.uade.tpo.grupo7.marketplace.common.enums.CouponDiscountType;
import com.uade.tpo.grupo7.marketplace.coupons.entity.Coupon;
import com.uade.tpo.grupo7.marketplace.coupons.repository.CouponRepository;

@Component
@Order(10)
public class CouponSeeder implements CommandLineRunner {

    private final CouponRepository couponRepository;

    public CouponSeeder(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @Override
    public void run(String... args) {
        if (couponRepository.count() > 0) {
            return;
        }

        couponRepository.save(Coupon.builder()
                .code("SKINDEX10")
                .discountType(CouponDiscountType.PERCENTAGE)
                .discountValue(BigDecimal.valueOf(10))
                .minimumSubtotal(BigDecimal.ZERO)
                .active(true)
                .validUntil(LocalDateTime.now().plusMonths(6))
                .build());

        couponRepository.save(Coupon.builder()
                .code("WELCOME500")
                .discountType(CouponDiscountType.FIXED_AMOUNT)
                .discountValue(BigDecimal.valueOf(500))
                .minimumSubtotal(BigDecimal.valueOf(5000))
                .active(true)
                .validUntil(LocalDateTime.now().plusMonths(6))
                .build());
    }
}