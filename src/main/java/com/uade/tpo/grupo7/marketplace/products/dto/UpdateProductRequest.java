package com.uade.tpo.grupo7.marketplace.products.dto;

public class UpdateProductRequest {

    private String name;
    private Double price;

    public UpdateProductRequest() {}

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