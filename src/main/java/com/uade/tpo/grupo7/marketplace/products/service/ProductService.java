package com.uade.tpo.grupo7.marketplace.products.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.uade.tpo.grupo7.marketplace.products.dto.CreateProductRequest;
import com.uade.tpo.grupo7.marketplace.products.dto.ProductDetailResponse;
import com.uade.tpo.grupo7.marketplace.products.dto.ProductResponse;
import com.uade.tpo.grupo7.marketplace.products.dto.UpdateProductRequest;
import com.uade.tpo.grupo7.marketplace.products.entity.Product;
import com.uade.tpo.grupo7.marketplace.products.entity.ProductImage;

public interface ProductService {

    Page<ProductResponse> getProductResponses(Pageable pageable, String search, List<Long> categoryIds, List<Long> colorIds, List<Long> sizeIds, Double minPrice, Double maxPrice);

    Page<ProductResponse> getMyProductResponses(Pageable pageable, Long userId);

    ProductDetailResponse getProductDetailResponseById(Long productId);

    ProductResponse createProductResponse(CreateProductRequest dto);

    ProductResponse updateProductResponse(Long productId, UpdateProductRequest dto);

    Page<Product> getProducts(Pageable pageable);

    Product getProductById(Long productId);

    Product createProduct(CreateProductRequest dto);

    Product updateProduct(Long productId, UpdateProductRequest dto);

    void deleteProduct(Long productId);

    ProductResponse setCoverImage(Long productId, Long imageId);

    List<ProductImage> uploadVariantImages(Long productId, Integer variantId, List<MultipartFile> files);

    void deleteVariantImage(Long productId, Integer variantId, Long imgId);
}
