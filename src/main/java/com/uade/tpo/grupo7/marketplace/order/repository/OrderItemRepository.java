package com.uade.tpo.grupo7.marketplace.order.repository;

import com.uade.tpo.grupo7.marketplace.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}