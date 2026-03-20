package com.uade.tpo.grupo9.marketplace.products.dto;

public class UpdateProductDto {

    private String name;
    private Double price;

    public UpdateProductDto() {}

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}