package com.uade.tpo.grupo9.marketplace.products.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    // TODO: add pagination and filters
    @GetMapping
    public Page<Product> getProducts(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        return this.productService.getProducts(pageable);
    }

    @GetMapping("{productId}")
    public Product getProductById(@PathVariable int productId) {
        return this.productService.getProductById(productId);
    }

    @PostMapping
    public Product createProduct(@Valid @RequestBody CreateProductDto dto) {
        return this.productService.createProduct(dto);
    }

}
