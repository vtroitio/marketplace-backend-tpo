package com.uade.tpo.grupo7.marketplace.products.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.uade.tpo.grupo7.marketplace.products.dto.CreateProductRequest;
import com.uade.tpo.grupo7.marketplace.products.dto.CreateProductVariantRequest;
import com.uade.tpo.grupo7.marketplace.products.dto.ProductResponse;
import com.uade.tpo.grupo7.marketplace.products.dto.UpdateProductRequest;
import com.uade.tpo.grupo7.marketplace.products.dto.UpdateProductVariantRequest;
import com.uade.tpo.grupo7.marketplace.products.dto.VariantAttributeValueRequest;
import com.uade.tpo.grupo7.marketplace.products.entity.AttributeValue;
import com.uade.tpo.grupo7.marketplace.products.entity.Category;
import com.uade.tpo.grupo7.marketplace.products.entity.Product;
import com.uade.tpo.grupo7.marketplace.products.entity.ProductImage;
import com.uade.tpo.grupo7.marketplace.products.entity.ProductVariant;
import com.uade.tpo.grupo7.marketplace.products.entity.VariantAttributeValue;
import com.uade.tpo.grupo7.marketplace.products.mapper.ProductMapper;
import com.uade.tpo.grupo7.marketplace.products.repository.AttributeValueRepository;
import com.uade.tpo.grupo7.marketplace.products.repository.CategoryRepository;
import com.uade.tpo.grupo7.marketplace.products.repository.ProductImageRepository;
import com.uade.tpo.grupo7.marketplace.products.repository.ProductRepository;
import com.uade.tpo.grupo7.marketplace.products.repository.ProductVariantRepository;

@Service
public class ProductServiceImpl implements ProductService {

    private static final int MAX_IMAGES = 10;

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductVariantRepository productVariantRepository;
    private final AttributeValueRepository attributeValueRepository;

    public ProductServiceImpl(
        ProductRepository productRepository,
        CategoryRepository categoryRepository,
        ProductImageRepository productImageRepository,
        ProductVariantRepository productVariantRepository,
        AttributeValueRepository attributeValueRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productImageRepository = productImageRepository;
        this.productVariantRepository = productVariantRepository;
        this.attributeValueRepository = attributeValueRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductResponse> getProductResponses(Pageable pageable) {
        return this.productRepository.findAll(pageable)
                .map(ProductMapper::toResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public ProductResponse getProductResponseById(Long productId) {
        return ProductMapper.toResponse(this.getProductById(productId));
    }

    @Transactional
    @Override
    public ProductResponse createProductResponse(CreateProductRequest dto) {
        return ProductMapper.toResponse(this.createProduct(dto));
    }

    @Transactional
    @Override
    public ProductResponse updateProductResponse(Long productId, UpdateProductRequest dto) {
        return ProductMapper.toResponse(this.updateProduct(productId, dto));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Product> getProducts(Pageable pageable) {
        return this.productRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Product getProductById(Long productId) throws ResponseStatusException {
        return this.productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found"
                ));
    }

    @Transactional
    @Override
    public Product createProduct(CreateProductRequest dto) {
        Product product = ProductMapper.toEntitiy(dto);
        product.setCategories(this.resolveCategories(dto.categoryIds()));
        product.setVariants(this.buildVariants(dto.variants(), product));

        return this.productRepository.save(product);
    }

    @Transactional
    @Override
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

        if (dto.variants() != null) {
            this.replaceProductVariants(product, this.mergeVariants(product, dto.variants()));
        }

        return this.productRepository.save(product);
    }

    @Transactional
    @Override
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

    private List<ProductVariant> buildVariants(List<CreateProductVariantRequest> variants, Product product) {
        if (variants == null || variants.isEmpty()) {
            return new ArrayList<>();
        }

        this.validateDuplicatedSkus(variants.stream().map(CreateProductVariantRequest::sku).toList());

        return variants.stream()
                .map(variantDto -> {
                    this.validateSkuAvailability(variantDto.sku(), null);
                    ProductVariant variant = ProductVariant.builder()
                            .sku(variantDto.sku())
                            .price(variantDto.price())
                            .stock(variantDto.stock())
                            .createdAt(LocalDateTime.now())
                            .product(product)
                            .build();
                    variant.setAttributeValues(this.buildVariantAttributeValues(variantDto.attributeValues(), variant));
                    return variant;
                })
                .toList();
    }

    private List<ProductVariant> mergeVariants(Product product, List<UpdateProductVariantRequest> variants) {
        if (variants.isEmpty()) {
            return new ArrayList<>();
        }

        this.validateDuplicatedSkus(variants.stream().map(UpdateProductVariantRequest::sku).toList());

        Map<Integer, ProductVariant> currentVariants = new HashMap<>();
        List<ProductVariant> existingVariants = product.getVariants() == null ? List.of() : product.getVariants();
        for (ProductVariant variant : existingVariants) {
            currentVariants.put(variant.getId(), variant);
        }

        List<ProductVariant> mergedVariants = new ArrayList<>();
        for (UpdateProductVariantRequest variantDto : variants) {
            ProductVariant variant;

            if (variantDto.id() != null) {
                variant = currentVariants.get(variantDto.id());
                if (variant == null) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Variant not found for product: " + variantDto.id()
                    );
                }
            } else {
                variant = ProductVariant.builder()
                        .createdAt(LocalDateTime.now())
                        .build();
            }

            this.validateSkuAvailability(variantDto.sku(), variant.getId());

            variant.setSku(variantDto.sku());
            if (variantDto.price() != null) {
                variant.setPrice(variantDto.price());
            }
            if (variantDto.stock() != null) {
                variant.setStock(variantDto.stock());
            }
            variant.setProduct(product);
            this.replaceVariantAttributeValues(
                    variant,
                    this.buildVariantAttributeValues(variantDto.attributeValues(), variant)
            );
            mergedVariants.add(variant);
        }

        return mergedVariants;
    }

    private void replaceProductVariants(Product product, List<ProductVariant> variants) {
        if (product.getVariants() == null) {
            product.setVariants(new ArrayList<>(variants));
            return;
        }

        product.getVariants().clear();
        product.getVariants().addAll(variants);
    }

    private void replaceVariantAttributeValues(
        ProductVariant variant,
        List<VariantAttributeValue> attributeValues
    ) {
        if (variant.getAttributeValues() == null) {
            variant.setAttributeValues(new ArrayList<>(attributeValues));
            return;
        }

        variant.getAttributeValues().clear();
        variant.getAttributeValues().addAll(attributeValues);
    }

    private List<VariantAttributeValue> buildVariantAttributeValues(
        List<VariantAttributeValueRequest> attributeValues,
        ProductVariant variant
    ) {
        if (attributeValues == null || attributeValues.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Each variant must include at least one attribute value"
            );
        }

        Set<Long> requestedIds = attributeValues.stream()
                .map(VariantAttributeValueRequest::attributeValueId)
                .collect(java.util.stream.Collectors.toSet());

        if (requestedIds.size() != attributeValues.size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Attribute values must not be duplicated within the same variant"
            );
        }

        List<AttributeValue> foundValues = this.attributeValueRepository.findAllById(requestedIds);
        if (foundValues.size() != requestedIds.size()) {
            Set<Long> foundIds = foundValues.stream()
                    .map(AttributeValue::getId)
                    .collect(java.util.stream.Collectors.toSet());
            Set<Long> missingIds = new HashSet<>(requestedIds);
            missingIds.removeAll(foundIds);
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Attribute values not found: " + missingIds
            );
        }

        Set<Long> attributeIds = foundValues.stream()
                .map(value -> value.getAttribute().getId())
                .collect(java.util.stream.Collectors.toSet());
        if (attributeIds.size() != foundValues.size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A variant cannot have two values for the same attribute"
            );
        }

        return foundValues.stream()
                .map(attributeValue -> VariantAttributeValue.builder()
                        .variant(variant)
                        .attributeValue(attributeValue)
                        .build())
                .toList();
    }

    private void validateDuplicatedSkus(List<String> skus) {
        Set<String> uniqueSkus = new HashSet<>(skus);
        if (uniqueSkus.size() != skus.size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Variant SKUs must be unique within the same product"
            );
        }
    }

    private void validateSkuAvailability(String sku, Integer variantId) {
        boolean alreadyExists = variantId == null
                ? this.productVariantRepository.existsBySku(sku)
                : this.productVariantRepository.existsBySkuAndIdNot(sku, variantId);

        if (alreadyExists) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Variant SKU already exists: " + sku
            );
        }
    }

    @Transactional
    @Override
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
    @Override
    public void deleteProductImage(Long productId, Long imgId) {
        this.productImageRepository.deleteById(imgId);

        List<ProductImage> images = this.productImageRepository
            .findByProductIdOrderByPositionAsc(productId);

        for (int i = 0; i < images.size(); i++) {
            images.get(i).setPosition(i);
        }

        this.productImageRepository.saveAll(images);
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