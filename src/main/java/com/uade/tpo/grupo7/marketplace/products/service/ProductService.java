package com.uade.tpo.grupo7.marketplace.products.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.uade.tpo.grupo7.marketplace.products.dto.CreateProductRequest;
import com.uade.tpo.grupo7.marketplace.products.dto.UpdateProductRequest;
import com.uade.tpo.grupo7.marketplace.products.entity.Category;
import com.uade.tpo.grupo7.marketplace.products.entity.Product;
import com.uade.tpo.grupo7.marketplace.products.mapper.ProductMapper;
import com.uade.tpo.grupo7.marketplace.products.repository.CategoryRepository;
import com.uade.tpo.grupo7.marketplace.products.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Page<Product> getProducts(Pageable pageable) {
        return this.productRepository.findAll(pageable);
    }

    public Product getProductById(Long productId) throws ResponseStatusException {
        return this.productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found"
                ));
    }

    public Product createProduct(CreateProductRequest dto) {
        Product product = ProductMapper.toEntitiy(dto);

        if (dto.categoryIds() != null && !dto.categoryIds().isEmpty()) {
            Set<Long> requestedCategoryIds = new HashSet<>(dto.categoryIds());
            List<Category> foundCategories = categoryRepository.findAllById(requestedCategoryIds);

            if (foundCategories.size() != requestedCategoryIds.size()) {
                Set<Long> foundCategoryIds = foundCategories.stream()
                        .map(Category::getId)
                        .collect(java.util.stream.Collectors.toSet());
                Set<Long> missingCategoryIds = new HashSet<>(requestedCategoryIds);
                missingCategoryIds.removeAll(foundCategoryIds);

                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Categories not found: " + missingCategoryIds
                );
            }

            Set<Category> categories = new HashSet<>(foundCategories);
            product.setCategories(categories);
        }

        return this.productRepository.save(product);
    }

    public Product updateProduct(Long productId, UpdateProductRequest dto) throws ResponseStatusException {
        Product product = this.getProductById(productId);

        if (dto.name() != null) {
            product.setName(dto.name());
        }

        if (dto.price() != null) {
            product.setPrice(dto.price());
        }

        if (dto.description() != null) {
            product.setDescription(dto.description());
        }

        return this.productRepository.save(product);
    }

    public void deleteProduct(Long productId) throws ResponseStatusException {
        this.getProductById(productId);
        this.productRepository.deleteById(productId);
    }
}
