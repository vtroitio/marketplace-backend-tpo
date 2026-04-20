package com.uade.tpo.grupo7.marketplace.order.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {
    private Long id;
    private Long productVariantId;
    private String productName; // Nombre del producto para que el front no tenga que buscarlo
    private Integer quantity;
    private BigDecimal unitPrice; // Precio al que se compró (histórico)
    private BigDecimal totalPrice;
}