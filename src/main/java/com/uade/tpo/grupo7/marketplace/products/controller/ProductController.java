package com.uade.tpo.grupo7.marketplace.products.controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.uade.tpo.grupo7.marketplace.products.dto.CreateProductRequest;
import com.uade.tpo.grupo7.marketplace.products.dto.ProductResponse;
import com.uade.tpo.grupo7.marketplace.products.dto.UpdateProductRequest;
import com.uade.tpo.grupo7.marketplace.products.entity.ProductImage;
import com.uade.tpo.grupo7.marketplace.products.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Parameter(name = "page", description = "NÃºmero de pÃ¡gina desde 0", example = "0")
    @Parameter(name = "size", description = "Cantidad de elementos por pÃ¡gina", example = "5")
    @Parameter(
        name = "sort",
        description = "Ordenamiento con formato campo,direccion. Ejemplo: id,asc o name,desc",
        example = "id,asc"
    )
    public Page<ProductResponse> getProducts(
            @ParameterObject @PageableDefault(page = 0, size = 5, sort = "id") Pageable pageable
    ) {
        return this.productService.getProductResponses(pageable);
    }

    @GetMapping("{productId}")
    @Operation(
        summary = "Obtener un producto por ID",
        description = "Obtiene los detalles de un producto especÃ­fico utilizando su ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    public ProductResponse getProductById(@PathVariable Long productId) {
        return this.productService.getProductResponseById(productId);
    }

    @PostMapping
    @Operation(
        summary = "Crear un nuevo producto",
        description = "Crea un nuevo producto en el marketplace"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado"),
        @ApiResponse(responseCode = "400", description = "Datos invÃ¡lidos")
    })
    public ProductResponse createProduct(
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Producto con variantes, categorias y valores de atributos",
                required = true,
                content = @Content(
                    schema = @Schema(implementation = CreateProductRequest.class),
                    examples = @ExampleObject(
                        name = "Producto con talle y color",
                        value = """
                        {
                          "name": "Remera azul lila prueba",
                          "price": 19999,
                          "description": "Remera de algodon con variante azul lila",
                          "categoryIds": [3],
                          "variants": [
                            {
                              "sku": "REM-AZL-TEST-M",
                              "price": 19999,
                              "stock": 10,
                              "attributeValues": [
                                {
                                  "attributeValueId": 2
                                },
                                {
                                  "attributeValueId": 10
                                }
                              ]
                            }
                          ]
                        }
                        """
                    )
                )
            )
            CreateProductRequest dto
    ) {
        return this.productService.createProductResponse(dto);
    }

    @PatchMapping("{productId}")
    @Operation(
        summary = "Actualizar un producto",
        description = "Actualiza parcialmente un producto existente"
    )
    public ProductResponse updateProduct(
            @PathVariable Long productId,
            @Valid
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Producto actualizado con variantes, categorias y valores de atributos",
                required = true,
                content = @Content(
                    schema = @Schema(implementation = UpdateProductRequest.class),
                    examples = @ExampleObject(
                        name = "Actualizar producto con talle y color",
                        value = """
                        {
                          "name": "Remera azul lila editada",
                          "price": 25000,
                          "description": "Remera editada desde Swagger",
                          "categoryIds": [3],
                          "variants": [
                            {
                              "id": 21,
                              "sku": "REM-AZL-TEST-M",
                              "price": 25000,
                              "stock": 15,
                              "attributeValues": [
                                {
                                  "attributeValueId": 2
                                },
                                {
                                  "attributeValueId": 10
                                }
                              ]
                            }
                          ]
                        }
                        """
                    )
                )
            )
            UpdateProductRequest dto
    ) {
        return this.productService.updateProductResponse(productId, dto);
    }

    @DeleteMapping("{productId}")
    @Operation(
        summary = "Eliminar un producto",
        description = "Elimina un producto del marketplace"
    )
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        this.productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "{productId}/images", consumes = "multipart/form-data")
    public List<ProductImage> uploadProductImage(
        @PathVariable Long productId,
        @RequestParam List<MultipartFile> files
    ) {
        return this.productService.uploadProductImages(productId, files);
    }

    @DeleteMapping("{productId}/images/{imgId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteProductImage(
        @PathVariable Long productId,
        @PathVariable Long imgId
    ) {
        this.productService.deleteProductImage(productId, imgId);
        return ResponseEntity.noContent().build();
    }
}
