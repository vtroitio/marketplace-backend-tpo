package com.uade.tpo.grupo9.marketplace.products.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {
    private Integer id;
    private String name;
    private Double price;
}
