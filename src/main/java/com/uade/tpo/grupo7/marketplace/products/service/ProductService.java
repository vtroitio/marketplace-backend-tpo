package com.uade.tpo.grupo7.marketplace.products.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.uade.tpo.grupo7.marketplace.products.dto.CreateProductRequest;
import com.uade.tpo.grupo7.marketplace.products.dto.UpdateProductRequest;
import com.uade.tpo.grupo7.marketplace.products.entity.Category;
import com.uade.tpo.grupo7.marketplace.products.entity.Product;
import com.uade.tpo.grupo7.marketplace.products.entity.ProductImage;
import com.uade.tpo.grupo7.marketplace.products.mapper.ProductMapper;
import com.uade.tpo.grupo7.marketplace.products.repository.CategoryRepository;
import com.uade.tpo.grupo7.marketplace.products.repository.ProductImageRepository;
import com.uade.tpo.grupo7.marketplace.products.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductService {

    private static final int MAX_IMAGES = 10;

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    public ProductService(
        ProductRepository productRepository,
        CategoryRepository categoryRepository,
        ProductImageRepository productImageRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productImageRepository = productImageRepository;
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
        product.setCategories(this.resolveCategories(dto.categoryIds()));

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

        if (dto.categoryIds() != null) {
            product.setCategories(this.resolveCategories(dto.categoryIds()));
        }

        return this.productRepository.save(product);
    }

    public void deleteProduct(Long productId) throws ResponseStatusException {
        this.getProductById(productId);
        this.productRepository.deleteById(productId);
    }

    private Set<Category> resolveCategories(List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return new HashSet<>();
        }

        Set<Long> requestedCategoryIds = new HashSet<>(categoryIds);
        List<Category> foundCategories = this.categoryRepository.findAllById(requestedCategoryIds);

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

        return new HashSet<>(foundCategories);
    }

    public List<ProductImage> uploadProductImages(Long productId, List<MultipartFile> files) {
        final int currentImages = this.productImageRepository.countByProductId(productId);

        if (currentImages + files.size() > MAX_IMAGES) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Cannot upload more than " + MAX_IMAGES + " images for a product."
            );
        }

        Product product = this.getProductById(productId);

        int position = currentImages;
        for (MultipartFile file : files) {
            try {
                String filePath = this.saveFile(file, productId);
                ProductImage productImage = ProductImage.builder()
                    .product(product)
                    .position(position)
                    .path(filePath)
                    .build();
                this.productImageRepository.save(productImage);
                position++;
            } catch (IOException e) {
                throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error saving file: " + file.getOriginalFilename(),
                    e
                );
            }
        }

        return this.productImageRepository.findAllByProductId(productId);
    }

    @Transactional
    public void deleteProductImage(Long productId, Long imgId) {
        this.productImageRepository.deleteById(imgId);

        List<ProductImage> images = this.productImageRepository
            .findByProductIdOrderByPositionAsc(productId);

        for (int i = 0; i < images.size(); i++) {
            images.get(i).setPosition(i);
        }
    }

    private String saveFile(MultipartFile file, Long productId) throws IOException {
        Path uploadPath = Paths.get(uploadDir, "products", productId.toString());

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalName = file.getOriginalFilename();

        String extension = Optional.ofNullable(originalName)
            .filter(name -> name.contains("."))
            .map(name -> name.substring(name.lastIndexOf(".")))
            .orElse("");

        String fileName = UUID.randomUUID() + extension;
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        return "/uploads/products/" + productId + "/" + fileName;
    }
}
