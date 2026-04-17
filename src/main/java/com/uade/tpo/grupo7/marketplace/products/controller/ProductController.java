package com.uade.tpo.grupo7.marketplace.products.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.uade.tpo.grupo7.marketplace.common.ErrorResponse;
import com.uade.tpo.grupo7.marketplace.products.dto.CreateProductRequest;
import com.uade.tpo.grupo7.marketplace.products.dto.UpdateProductRequest;
import com.uade.tpo.grupo7.marketplace.products.entity.Product;
import com.uade.tpo.grupo7.marketplace.products.entity.ProductImage;
import com.uade.tpo.grupo7.marketplace.products.service.ProductService;

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
        summary = "Obtener los productos paginados",
        description = "Obtiene una lista paginada de productos disponibles en el marketplace. Se pueden especificar parámetros de paginación como número de página, tamaño de página y ordenamiento"
    )
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente")
    public Page<Product> getProducts(
            @PageableDefault(page = 0, size = 5) Pageable pageable
    ) {
        return this.productService.getProducts(pageable);
    }

    @GetMapping("{productId}")
    @Operation(
        summary = "Obtener un producto por ID",
        description = "Obtiene los detalles de un producto específico utilizando su ID"
    )    
    @ApiResponses(value={
        @ApiResponse(responseCode="200",description="Producto encontrado"),
        @ApiResponse(
            responseCode="404",
            description="Producto no encontrado",
            content=@Content(
                mediaType="application/json",
                schema=@Schema(implementation=ErrorResponse.class)))
    })
    public Product getProductById(@PathVariable int productId) {
        return this.productService.getProductById(productId);
    }

    @PostMapping
    @Operation(
        summary = "Crear un nuevo producto",
        description = "Crea un nuevo producto en el marketplace con los datos proporcionados"
    )
    @ApiResponses(value={
        @ApiResponse(responseCode="201",description="Producto creado"),
        @ApiResponse(
            responseCode="400",
            description="Datos de producto inválidos",
            content=@Content(
                mediaType="application/json",
                schema=@Schema(implementation=ErrorResponse.class))
        )
    })
    public Product createProduct(@Valid @RequestBody CreateProductRequest dto) {
        return this.productService.createProduct(dto);
    }

    @PatchMapping("{productId}")
    @Operation(
        summary = "Actualizar un producto existente",
        description = "Actualiza los detalles de un producto existente utilizando su ID y los datos proporcionados"
    )
    @ApiResponses(value={
        @ApiResponse(responseCode="200",description="Producto actualizado"),
        @ApiResponse(
            responseCode="400",
            description="Datos de producto inválidos",
            content=@Content(
                mediaType="application/json",
                schema=@Schema(implementation=ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode="404",
            description="Producto no encontrado",
            content=@Content(
                mediaType="application/json",
                schema=@Schema(implementation=ErrorResponse.class))
        )
    })
    public Product updateProduct(
            @PathVariable int productId,
            @RequestBody UpdateProductRequest dto) {
        return this.productService.updateProduct(productId, dto);
    }

    @DeleteMapping("{productId}")
    @Operation(
        summary = "Eliminar un producto",
        description = "Elimina un producto del marketplace utilizando su ID"
    )
    @ApiResponses(value={
        @ApiResponse(responseCode="204",description="Producto eliminado"),
        @ApiResponse(
            responseCode="404",
            description="Producto no encontrado",
            content=@Content(
                mediaType="application/json",
                schema=@Schema(implementation=ErrorResponse.class))
        )
    })
    public ResponseEntity<Void> deleteProduct(@PathVariable int productId) {
        this.productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('SELLER')")
    @PostMapping(value = "{productId}/images", consumes = "multipart/form-data")
    public List<ProductImage> uploadProductImages(
        @PathVariable int productId,
        @RequestParam List<MultipartFile> files
    ) {
        return this.productService.uploadProductImages(productId, files);
    }

    @PreAuthorize("hasRole('SELLER')")
    @PutMapping("{productId}/images/order")
    public ResponseEntity<Void> reorderProductImages(
        @PathVariable int productId,
        @RequestBody List<Long> orderedIds
    ) {
        this.productService.reorderProductImages(productId, orderedIds);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('SELLER')")
    @DeleteMapping("{productId}/images/{imgId}")
    @ApiResponse(responseCode="204")
    public ResponseEntity<Void> deleteProductImage(
        @PathVariable int productId,
        @PathVariable Long imgId
    ) {        
        this.productService.deleteProductImage(productId, imgId);
        return ResponseEntity.noContent().build();
    }

}