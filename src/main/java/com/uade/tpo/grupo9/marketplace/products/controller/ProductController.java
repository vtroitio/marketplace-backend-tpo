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

import com.uade.tpo.grupo9.marketplace.products.dto.CreateProductRequest;
import com.uade.tpo.grupo9.marketplace.products.entity.Product;
import com.uade.tpo.grupo9.marketplace.products.service.ProductService;
import com.uade.tpo.grupo9.marketplace.common.exception.ErrorResponse;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

@RestController
@RequestMapping("products")
@Tag(name = "Products", description = "Endpoints de productos del marketplace")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(
        summary = "Obtener todos los productos", 
        description = "Obtiene una lista de todos los productos disponibles en el marketplace"
    )
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente")
    public List<Product> getProducts() {
        return this.productService.getProducts();
    }

    @GetMapping("{productId}")
    @Operation(
        summary = "Obtener un producto por ID", 
        description = "Obtiene los detalles de un producto específico utilizando su ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado"),
            @ApiResponse(
                responseCode = "404",
                description = "Producto no encontrado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
                )
            )
    })
    public Product getProductById(@PathVariable int productId) {
        return this.productService.getProductById(productId);
    }

    @PostMapping
    @Operation(
        summary = "Crear un nuevo producto", 
        description = "Crea un nuevo producto en el marketplace con los datos proporcionados"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Producto creado"),
            @ApiResponse(
                responseCode = "400",
                description = "Datos de producto inválidos",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorResponse.class)
            )
        )
    })
    public Product createProduct(@Valid @RequestBody CreateProductRequest dto) {
        return this.productService.createProduct(dto);
    }

    @DeleteMapping("{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int productId) {
        this.productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
