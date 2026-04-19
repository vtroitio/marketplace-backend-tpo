package com.uade.tpo.grupo7.marketplace.cart.repository;

import com.uade.tpo.grupo7.marketplace.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}