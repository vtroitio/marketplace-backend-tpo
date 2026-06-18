package com.uade.tpo.grupo7.marketplace.order.service;

import com.uade.tpo.grupo7.marketplace.coupons.service.CouponService;
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
import com.uade.tpo.grupo7.marketplace.order.dto.CheckoutRequest;
import com.uade.tpo.grupo7.marketplace.order.dto.OrderItemResponse;
import com.uade.tpo.grupo7.marketplace.order.dto.OrderResponse;
import com.uade.tpo.grupo7.marketplace.order.entity.OrderItem;
import com.uade.tpo.grupo7.marketplace.order.entity.PurchaseOrder;
import com.uade.tpo.grupo7.marketplace.order.repository.PurchaseOrderRepository;
import com.uade.tpo.grupo7.marketplace.products.entity.ProductVariant;

@Service
public class OrderServiceImpl implements OrderService {

    private final CouponService couponService;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final CartRepository cartRepository;

    public OrderServiceImpl(PurchaseOrderRepository purchaseOrderRepository,
            CartRepository cartRepository, CouponService couponService) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.cartRepository = cartRepository;
        this.couponService = couponService;
    }

    @Transactional
    @Override
    public OrderResponse checkout(Long userId, CheckoutRequest request) {
        Cart cart = cartRepository.findByBuyerId(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cart is empty");
        }

        BigDecimal subtotal = BigDecimal.ZERO;

        PurchaseOrder order = PurchaseOrder.builder()
                .buyer(cart.getBuyer())
                .status(OrderStatus.PENDING)
                .subtotalAmount(BigDecimal.ZERO)
                .discountAmount(BigDecimal.ZERO)
                .shippingCost(BigDecimal.valueOf(15.00))
                .totalAmount(BigDecimal.ZERO)
                .couponCode(null)
                .shippingFullName(request.fullName())
                .shippingAddress(request.address())
                .shippingCity(request.city())
                .shippingPostalCode(request.postalCode())
                .paymentMethod("CARD")
                .paymentStatus("APPROVED")
                .cardLastFour(getCardLastFour(request.cardNumber()))
                .build();

        for (CartItem cartItem : cart.getItems()) {
            ProductVariant variant = cartItem.getProductVariant();
            int requested = cartItem.getQuantity();

            if (variant.getStock() == null || variant.getStock() < requested) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Insufficient stock for variant " + variant.getSku());
            }

            if (!variant.getProduct().isActive()) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Product is not available: " + variant.getProduct().getName());
            }

            if (variant.getProduct().getSeller().getId().equals(userId)) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "You cannot buy your own product");
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

            subtotal = subtotal.add(linePrice);
        }

        BigDecimal discountAmount = couponService.calculateDiscountForCheckout(
                request.couponCode(),
                subtotal);

        BigDecimal shippingCost = BigDecimal.valueOf(15.00);

        BigDecimal total = subtotal
                .subtract(discountAmount)
                .add(shippingCost);

        order.setSubtotalAmount(subtotal);
        order.setDiscountAmount(discountAmount);
        order.setShippingCost(shippingCost);
        order.setTotalAmount(total);

        if (request.couponCode() != null && !request.couponCode().isBlank()) {
            order.setCouponCode(request.couponCode().trim().toUpperCase());
        }

        for (CartItem cartItem : cart.getItems()) {
            ProductVariant variant = cartItem.getProductVariant();
            variant.setStock(variant.getStock() - cartItem.getQuantity());
        }

        PurchaseOrder savedOrder = purchaseOrderRepository.save(order);

        cart.getItems().clear();
        cart.setTotalAmount(BigDecimal.ZERO);
        cartRepository.save(cart);

        return mapToResponse(savedOrder);
    }

    private String getCardLastFour(String cardNumber) {
        if (cardNumber == null || cardNumber.isBlank()) {
            return null;
        }

        String normalized = cardNumber.replaceAll("\\s+", "");

        if (normalized.length() < 4) {
            return normalized;
        }

        return normalized.substring(normalized.length() - 4);
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
                order.getSubtotalAmount(),
                order.getDiscountAmount(),
                order.getShippingCost(),
                order.getTotalAmount(),
                order.getCouponCode(),
                order.getShippingFullName(),
                order.getShippingAddress(),
                order.getShippingCity(),
                order.getShippingPostalCode(),
                order.getPaymentMethod(),
                order.getPaymentStatus(),
                order.getCardLastFour(),
                order.getCreatedAt(),
                order.getItems().stream().map(this::mapItem).collect(Collectors.toList()));
    }

    private OrderItemResponse mapItem(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getProductVariant().getId(),
                item.getProductVariant().getProduct().getName(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getTotalPrice());
    }
}