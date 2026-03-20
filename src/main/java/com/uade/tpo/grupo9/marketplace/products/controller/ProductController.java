package com.uade.tpo.grupo9.marketplace.products.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.grupo9.marketplace.products.dto.CreateProductDto;
import com.uade.tpo.grupo9.marketplace.products.entity.Product;
import com.uade.tpo.grupo9.marketplace.products.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getProducts() {
        return this.productService.getProducts();
    }

    @GetMapping("{productId}")
    public Product getProductById(@PathVariable int productId) {
        return this.productService.getProductById(productId);
    }

    @PostMapping
    public Product createProduct(@Valid @RequestBody CreateProductDto dto) {
        return this.productService.createProduct(dto);
    }

    @DeleteMapping("{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int productId) {
        this.productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
