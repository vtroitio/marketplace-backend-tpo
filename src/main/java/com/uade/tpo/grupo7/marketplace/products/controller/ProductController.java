package com.uade.tpo.grupo7.marketplace.products.controller;

import java.util.List;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.uade.tpo.grupo7.marketplace.products.dto.ProductDetailResponse;
import com.uade.tpo.grupo7.marketplace.products.dto.ProductResponse;
import com.uade.tpo.grupo7.marketplace.products.dto.UpdateProductRequest;
import com.uade.tpo.grupo7.marketplace.products.entity.ProductImage;
import com.uade.tpo.grupo7.marketplace.products.service.ProductService;
import com.uade.tpo.grupo7.marketplace.users.entity.User;

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
  @Operation(summary = "Obtener los productos paginados", description = "Obtiene una lista paginada de productos disponibles en el marketplace")
  @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente")
  @Parameter(name = "page", description = "NÃºmero de pÃ¡gina desde 0", example = "0")
  @Parameter(name = "size", description = "Cantidad de elementos por pÃ¡gina", example = "5")
  @Parameter(name = "sort", description = "Ordenamiento con formato campo,direccion. Ejemplo: id,asc o name,desc", example = "id,asc")
  public Page<ProductResponse> getProducts(
      @ParameterObject @PageableDefault(page = 0, size = 5, sort = "id") Pageable pageable,
      @RequestParam(required = false) String search,
      @RequestParam(required = false) List<Long> categoryIds,
      @RequestParam(required = false) List<Long> colorIds,
      @RequestParam(required = false) List<Long> sizeIds,
      @RequestParam(required = false) Double minPrice,
      @RequestParam(required = false) Double maxPrice) {
    return this.productService.getProductResponses(
        pageable,
        search,
        categoryIds,
        colorIds,
        sizeIds,
        minPrice,
        maxPrice);
  }

  @GetMapping("me")
  @PreAuthorize("hasRole('SELLER')")
  public Page<ProductResponse> getMyProducts(
      @ParameterObject @PageableDefault(page = 0, size = 5, sort = "id") Pageable pageable,
      @AuthenticationPrincipal User user) {
    return this.productService.getMyProductResponses(pageable, user.getId());
  }

  @GetMapping("{productId}")
  @Operation(summary = "Obtener un producto por ID", description = "Obtiene los detalles de un producto especÃ­fico utilizando su ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Producto encontrado"),
      @ApiResponse(responseCode = "404", description = "Producto no encontrado")
  })
  public ProductDetailResponse getProductById(@PathVariable Long productId) {
    return this.productService.getProductDetailResponseById(productId);
  }

  @PreAuthorize("""
          hasRole('ADMIN') or
          (hasRole('SELLER') and @ownership.isProductOwner(#productId, authentication.principal))
      """)
  @GetMapping("{productId}/edit")
  public ProductDetailResponse getProductForEdit(@PathVariable Long productId) {
    return this.productService.getProductDetailResponseById(productId);
  }

  @PreAuthorize("hasRole('SELLER')")
  @PostMapping
  @Operation(summary = "Crear un nuevo producto", description = "Crea un nuevo producto en el marketplace")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Producto creado"),
      @ApiResponse(responseCode = "400", description = "Datos invÃ¡lidos")
  })
  public ResponseEntity<ProductResponse> createProduct(
      @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Producto con variantes, categorias y valores de atributos", required = true, content = @Content(schema = @Schema(implementation = CreateProductRequest.class), examples = @ExampleObject(name = "Producto con talle y color", value = """
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
          """))) CreateProductRequest dto,
      @AuthenticationPrincipal User user) {
    return ResponseEntity.status(HttpStatus.CREATED).body(this.productService.createProductResponse(dto, user));
  }

  @PreAuthorize("""
          hasRole('ADMIN') or
          (hasRole('SELLER') and @ownership.isProductOwner(#productId, authentication.principal))
      """)
  @PatchMapping("{productId}")
  @Operation(summary = "Actualizar un producto", description = "Actualiza parcialmente un producto existente")
  public ProductResponse updateProduct(
      @PathVariable Long productId,
      @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Producto actualizado con variantes, categorias y valores de atributos", required = true, content = @Content(schema = @Schema(implementation = UpdateProductRequest.class), examples = @ExampleObject(name = "Actualizar producto con talle y color", value = """
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
          """))) UpdateProductRequest dto) {
    return this.productService.updateProductResponse(productId, dto);
  }

  @PreAuthorize("""
          hasRole('ADMIN') or
          (hasRole('SELLER') and @ownership.isProductOwner(#productId, authentication.principal))
      """)
  @DeleteMapping("{productId}")
  @Operation(summary = "Eliminar un producto", description = "Elimina un producto del marketplace")
  public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
    this.productService.deleteProduct(productId);
    return ResponseEntity.noContent().build();
  }

  @PreAuthorize("""
          hasRole('ADMIN') or
          (hasRole('SELLER') and @ownership.isProductOwner(#productId, authentication.principal))
      """)
  @PatchMapping("{productId}/activate")
  @Operation(summary = "Activar un producto", description = "Vuelve a publicar un producto desactivado")
  public ProductResponse activateProduct(@PathVariable Long productId) {
    return this.productService.activateProduct(productId);
  }

  @PreAuthorize("""
          hasRole('ADMIN') or
          (hasRole('SELLER') and @ownership.isProductOwner(#productId, authentication.principal))
      """)
  @PatchMapping("{productId}/deactivate")
  @Operation(summary = "Desactivar un producto", description = "Oculta un producto del marketplace sin eliminarlo")
  public ProductResponse deactivateProduct(@PathVariable Long productId) {
    return this.productService.deactivateProduct(productId);
  }

  @PreAuthorize("""
          hasRole('ADMIN') or
          (hasRole('SELLER') and @ownership.isProductOwner(#productId, authentication.principal))
      """)
  @PatchMapping("{productId}/cover-image/{imageId}")
  public ProductResponse setCoverImage(
      @PathVariable Long productId,
      @PathVariable Long imageId) {
    return this.productService.setCoverImage(productId, imageId);
  }

  @PreAuthorize("""
          hasRole('ADMIN') or
          (hasRole('SELLER') and @ownership.isProductOwner(#productId, authentication.principal))
      """)
  @PostMapping(value = "{productId}/variants/{variantId}/images", consumes = "multipart/form-data")
  public List<ProductImage> uploadVariantImages(
      @PathVariable Long productId,
      @PathVariable Integer variantId,
      @RequestParam List<MultipartFile> files) {
    return this.productService.uploadVariantImages(productId, variantId, files);
  }

  @PreAuthorize("""
          hasRole('ADMIN') or
          (hasRole('SELLER') and @ownership.isProductOwner(#productId, authentication.principal))
      """)
  @DeleteMapping("{productId}/variants/{variantId}/images/{imgId}")
  @ApiResponse(responseCode = "204")
  public ResponseEntity<Void> deleteVariantImage(
      @PathVariable Long productId,
      @PathVariable Integer variantId,
      @PathVariable Long imgId) {
    this.productService.deleteVariantImage(productId, variantId, imgId);
    return ResponseEntity.noContent().build();
  }

  @PreAuthorize("""
          hasRole('ADMIN') or
          (hasRole('SELLER') and @ownership.isProductOwner(#productId, authentication.principal))
      """)
  @PatchMapping("{productId}/variants/{variantId}/images/reorder")
  @ApiResponse(responseCode = "204")
  public ResponseEntity<Void> reorderVariantImages(
      @PathVariable Long productId,
      @PathVariable Integer variantId,
      @RequestBody List<Long> imageIds) {
    this.productService.reorderVariantImages(productId, variantId, imageIds);
    return ResponseEntity.noContent().build();
  }
}
