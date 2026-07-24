package com.uade.tpo.grupo7.marketplace.order.service;

import java.util.List;

import com.uade.tpo.grupo7.marketplace.order.dto.CheckoutRequest;
import com.uade.tpo.grupo7.marketplace.order.dto.OrderResponse;

public interface OrderService {

    OrderResponse checkout(Long userId, CheckoutRequest request);

    List<OrderResponse> getMyOrders(Long userId);

    OrderResponse getOrderById(Long userId, Long orderId);
}
