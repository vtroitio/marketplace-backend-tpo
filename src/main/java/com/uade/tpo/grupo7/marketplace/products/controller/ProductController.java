package com.uade.tpo.grupo7.marketplace.products.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uade.tpo.grupo7.marketplace.products.dto.CreateProductRequest;
import com.uade.tpo.grupo7.marketplace.products.dto.ProductResponse;
import com.uade.tpo.grupo7.marketplace.products.dto.UpdateProductRequest;
import com.uade.tpo.grupo7.marketplace.products.entity.Product;
import com.uade.tpo.grupo7.marketplace.products.mapper.ProductMapper;
import com.uade.tpo.grupo7.marketplace.products.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
        summary = "Obtener los productos paginados",
        description = "Obtiene una lista paginada de productos disponibles en el marketplace"
    )
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente")
    public Page<ProductResponse> getProducts(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        return this.productService.getProducts(pageable)
                .map(ProductMapper::toResponse);
    }

    @GetMapping("{productId}")
    @Operation(
        summary = "Obtener un producto por ID",
        description = "Obtiene los detalles de un producto específico utilizando su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ProductResponse getProductById(@PathVariable int productId) {
        Product product = this.productService.getProductById(productId);

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getCreatedAt(),
                product.getDeletedAt()
        );
    }

    @PostMapping
    @Operation(
        summary = "Crear un nuevo producto",
        description = "Crea un nuevo producto en el marketplace"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public ProductResponse createProduct(@Valid @RequestBody CreateProductRequest dto) {
        Product product = this.productService.createProduct(dto);
        return ProductMapper.toResponse(product);
    }

    @PatchMapping("{productId}")
    @Operation(
        summary = "Actualizar un producto",
        description = "Actualiza parcialmente un producto existente"
    )
    public ProductResponse updateProduct(
            @PathVariable int productId,
            @Valid @RequestBody UpdateProductRequest dto
    ) {
        Product product = this.productService.updateProduct(productId, dto);
        return ProductMapper.toResponse(product);
    }

    @DeleteMapping("{productId}")
    @Operation(
        summary = "Eliminar un producto",
        description = "Elimina un producto del marketplace"
    )
    public ResponseEntity<Void> deleteProduct(@PathVariable int productId) {
        this.productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}