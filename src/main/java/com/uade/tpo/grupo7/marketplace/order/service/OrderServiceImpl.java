package com.uade.tpo.grupo7.marketplace.order.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.uade.tpo.grupo7.marketplace.cart.entity.Cart;
import com.uade.tpo.grupo7.marketplace.cart.entity.CartItem;
import com.uade.tpo.grupo7.marketplace.cart.repository.CartRepository;
import com.uade.tpo.grupo7.marketplace.common.enums.OrderStatus;
import com.uade.tpo.grupo7.marketplace.order.dto.OrderItemResponse;
import com.uade.tpo.grupo7.marketplace.order.dto.OrderResponse;
import com.uade.tpo.grupo7.marketplace.order.entity.OrderItem;
import com.uade.tpo.grupo7.marketplace.order.entity.PurchaseOrder;
import com.uade.tpo.grupo7.marketplace.order.repository.PurchaseOrderRepository;
import com.uade.tpo.grupo7.marketplace.products.entity.ProductVariant;

@Service
public class OrderServiceImpl implements OrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final CartRepository cartRepository;

    public OrderServiceImpl(PurchaseOrderRepository purchaseOrderRepository,
                        CartRepository cartRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.cartRepository = cartRepository;
    }

    @Transactional
    @Override
    public OrderResponse checkout(Long userId) {
        Cart cart = cartRepository.findByBuyerId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");
        }

        PurchaseOrder order = PurchaseOrder.builder()
                .buyer(cart.getBuyer())
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : cart.getItems()) {
            ProductVariant variant = cartItem.getProductVariant();
            int requested = cartItem.getQuantity();

            if (variant.getStock() == null || variant.getStock() < requested) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Insufficient stock for variant " + variant.getSku()
                );
            }

            BigDecimal unitPrice = BigDecimal.valueOf(variant.getPrice());
            BigDecimal linePrice = unitPrice.multiply(BigDecimal.valueOf(requested));

            OrderItem orderItem = OrderItem.builder()
                    .purchaseOrder(order)
                    .productVariant(variant)
                    .quantity(requested)
                    .unitPrice(unitPrice)
                    .totalPrice(linePrice)
                    .build();

            order.getItems().add(orderItem);
            variant.setStock(variant.getStock() - requested);
            total = total.add(linePrice);
        }

        order.setTotalAmount(total);
        PurchaseOrder savedOrder = purchaseOrderRepository.save(order);

        cart.getItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.save(cart);

        return mapToResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    @Override
    public List<OrderResponse> getMyOrders(Long userId) {
        return purchaseOrderRepository.findByBuyerIdOrderByCreatedAtDesc(userId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public OrderResponse getOrderById(Long userId, Long orderId) {
        PurchaseOrder order = purchaseOrderRepository.findByIdAndBuyerId(orderId, userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        return mapToResponse(order);
    }

    private OrderResponse mapToResponse(PurchaseOrder order) {
        return new OrderResponse(
                order.getId(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                order.getItems().stream().map(this::mapItem).collect(Collectors.toList())
        );
    }

    private OrderItemResponse mapItem(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getProductVariant().getId(),
                item.getProductVariant().getProduct().getName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice()
        );
    }
}