package com.uade.tpo.grupo7.marketplace.order.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.grupo7.marketplace.order.dto.OrderResponse;
import com.uade.tpo.grupo7.marketplace.order.service.OrderService;
import com.uade.tpo.grupo7.marketplace.users.entity.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("orders")
@Tag(name = "Orders", description = "Endpoints de órdenes de compra del usuario autenticado")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("hasRole('BUYER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Confirmar compra a partir del carrito actual")
    public OrderResponse checkout(@AuthenticationPrincipal User principal) {
        return orderService.checkout(currentUserId(principal));
    }

    @PreAuthorize("hasRole('BUYER')")
    @GetMapping
    @Operation(summary = "Listar las órdenes del usuario autenticado")
    public List<OrderResponse> getMyOrders(@AuthenticationPrincipal User principal) {
        return orderService.getMyOrders(currentUserId(principal));
    }

    @PreAuthorize("hasRole('BUYER')")
    @GetMapping("{orderId}")
    @Operation(summary = "Obtener una orden por ID")
    public OrderResponse getOrderById(
            @AuthenticationPrincipal User principal,
            @PathVariable Long orderId
    ) {
        return orderService.getOrderById(currentUserId(principal), orderId);
    }

    private Long currentUserId(User principal) {
        return principal.getId();   
    }
}
