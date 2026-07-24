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
import com.uade.tpo.grupo7.marketplace.products.dto.ProductDetailResponse;
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
import com.uade.tpo.grupo7.marketplace.users.entity.User;

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
            AttributeValueRepository attributeValueRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productImageRepository = productImageRepository;
        this.productVariantRepository = productVariantRepository;
        this.attributeValueRepository = attributeValueRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductResponse> getProductResponses(
            Pageable pageable,
            String search,
            List<Long> categoryIds,
            List<Long> colorIds,
            List<Long> sizeIds,
            Double minPrice,
            Double maxPrice) {
        search = normalizeSearch(search);
        categoryIds = nullIfEmpty(categoryIds);
        colorIds = nullIfEmpty(colorIds);
        sizeIds = nullIfEmpty(sizeIds);

        boolean hasFilters = search != null ||
                categoryIds != null ||
                colorIds != null ||
                sizeIds != null ||
                minPrice != null ||
                maxPrice != null;

        Page<Product> products = hasFilters
                ? this.productRepository.findWithFilters(
                        search,
                        categoryIds,
                        colorIds,
                        sizeIds,
                        minPrice,
                        maxPrice,
                        pageable)
                : this.productRepository.findByActiveTrueAndDeletedAtIsNull(pageable);

        return products.map(ProductMapper::toResponse);
    }

    private String normalizeSearch(String search) {
        if (search == null || search.isBlank()) {
            return null;
        }

        return search.trim().toLowerCase();
    }

    private <T> List<T> nullIfEmpty(List<T> list) {
        return list == null || list.isEmpty() ? null : list;
    }

    @Transactional(readOnly = true)
    @Override
    public ProductDetailResponse getProductDetailResponseById(Long productId) {
        return ProductMapper.toDetailResponse(this.getProductById(productId));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ProductResponse> getMyProductResponses(Pageable pageable, Long userId) {
        return this.productRepository.findBySellerIdAndDeletedAtIsNull(pageable, userId)
                .map(ProductMapper::toResponse);
    }

    @Transactional
    @Override
    public ProductResponse createProductResponse(CreateProductRequest dto, User user) {
        return ProductMapper.toResponse(this.createProduct(dto, user));
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
                        "Producto no encontrado"));
    }

    @Transactional
    @Override
    public Product createProduct(CreateProductRequest dto, User user) {
        Product product = ProductMapper.toEntitiy(dto);
        product.setSeller(user);
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
        Product product = this.getProductById(productId);
        product.softDelete();
        this.productRepository.save(product);
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
                    "Categorías no encontradas: " + missingCategoryIds);
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
                            "Variante no encontrada para el producto: " + variantDto.id());
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
                    this.buildVariantAttributeValues(variantDto.attributeValues(), variant));
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
            List<VariantAttributeValue> attributeValues) {
        if (variant.getAttributeValues() == null) {
            variant.setAttributeValues(new ArrayList<>(attributeValues));
            return;
        }

        variant.getAttributeValues().clear();
        variant.getAttributeValues().addAll(attributeValues);
    }

    private List<VariantAttributeValue> buildVariantAttributeValues(
            List<VariantAttributeValueRequest> attributeValues,
            ProductVariant variant) {
        if (attributeValues == null || attributeValues.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cada variante debe incluir al menos un valor de atributo");
        }

        Set<Long> requestedIds = attributeValues.stream()
                .map(VariantAttributeValueRequest::attributeValueId)
                .collect(java.util.stream.Collectors.toSet());

        if (requestedIds.size() != attributeValues.size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Los valores de atributos no deben duplicarse dentro de la misma variante");
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
                    "Valores de atributos no encontrados: " + missingIds);
        }

        Set<Long> attributeIds = foundValues.stream()
                .map(value -> value.getAttribute().getId())
                .collect(java.util.stream.Collectors.toSet());
        if (attributeIds.size() != foundValues.size()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Una variante no puede tener dos valores para el mismo atributo");
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
                    "Los SKU de variantes deben ser únicos dentro del mismo producto");
        }
    }

    private void validateSkuAvailability(String sku, Integer variantId) {
        boolean alreadyExists = variantId == null
                ? this.productVariantRepository.existsBySku(sku)
                : this.productVariantRepository.existsBySkuAndIdNot(sku, variantId);

        if (alreadyExists) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El SKU de variante ya existe: " + sku);
        }
    }

    @Override
    public ProductResponse setCoverImage(Long productId, Long imageId) {
        Product product = this.productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Producto no encontrado"));

        ProductImage image = this.productImageRepository.findById(imageId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Imagen no encontrada"));

        ProductVariant variant = image.getVariant();

        if (variant == null || variant.getProduct() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La imagen no está asociada con una variante válida");
        }

        if (!variant.getProduct().getId().equals(productId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La imagen no pertenece a este producto");
        }

        product.setCoverImagePath(image.getPath());

        Product savedProduct = this.productRepository.save(product);

        return ProductMapper.toResponse(savedProduct);
    }

    @Transactional
    @Override
    public List<ProductImage> uploadVariantImages(
            Long productId,
            Integer variantId,
            List<MultipartFile> files) {
        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Variante no encontrada"));

        if (!variant.getProduct().getId().equals(productId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La variante no pertenece a este producto");
        }

        final int currentImages = this.productImageRepository.countByVariantId(variantId);

        if (currentImages + files.size() > MAX_IMAGES) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se pueden cargar más de " + MAX_IMAGES + " imágenes para una variante.");
        }

        int position = currentImages;

        for (MultipartFile file : files) {
            try {
                String filePath = this.saveFile(file, productId);

                ProductImage productImage = ProductImage.builder()
                        .variant(variant)
                        .position(position)
                        .path(filePath)
                        .build();

                this.productImageRepository.save(productImage);
                position++;

            } catch (IOException e) {
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Error al guardar el archivo: " + file.getOriginalFilename(),
                        e);
            }
        }

        return this.productImageRepository.findAllByVariantIdOrderByPositionAsc(variantId);
    }

    @Transactional
    @Override
    public void deleteVariantImage(Long productId, Integer variantId, Long imgId) {
        ProductImage image = this.productImageRepository.findById(imgId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Imagen no encontrada"));

        ProductVariant variant = image.getVariant();

        if (!variant.getId().equals(variantId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La imagen no pertenece a esta variante");
        }

        Product product = variant.getProduct();

        if (!product.getId().equals(productId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La variante no pertenece a este producto");
        }

        String deletedImagePath = image.getPath();

        this.productImageRepository.delete(image);
        deleteFile(deletedImagePath);

        List<ProductImage> variantImages = this.productImageRepository
                .findAllByVariantIdOrderByPositionAsc(variantId);

        for (int i = 0; i < variantImages.size(); i++) {
            variantImages.get(i).setPosition(i);
        }

        this.productImageRepository.saveAll(variantImages);

        if (deletedImagePath.equals(product.getCoverImagePath())) {
            List<ProductImage> remainingImages = this.productImageRepository
                    .findAllByVariantProductIdOrderByPositionAsc(productId);

            product.setCoverImagePath(
                    remainingImages.isEmpty() ? null : remainingImages.get(0).getPath());

            this.productRepository.save(product);
        }
    }

    @Transactional
    @Override
    public void reorderVariantImages(Long productId, Integer variantId, List<Long> imageIds) {
        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Variante no encontrada"));

        if (!variant.getProduct().getId().equals(productId)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "La variante no pertenece a este producto");
        }

        List<ProductImage> images = this.productImageRepository
                .findAllByVariantIdOrderByPositionAsc(variantId);

        for (int i = 0; i < imageIds.size(); i++) {
            final Long imageId = imageIds.get(i);
            final int newPosition = i;
            images.stream()
                    .filter(img -> img.getId().equals(imageId))
                    .findFirst()
                    .ifPresent(img -> img.setPosition(newPosition));
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

    private void deleteFile(String imagePath) {
        try {
            String relativePath = imagePath.replaceFirst("^/uploads/", "");
            Path filePath = Paths.get(uploadDir).resolve(relativePath);

            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al eliminar el archivo de imagen",
                    e);
        }
    }

    @Transactional
    @Override
    public ProductResponse activateProduct(Long productId) {
        Product product = this.getProductById(productId);

        if (product.getDeletedAt() != null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No se puede activar un producto eliminado");
        }

        product.activate();

        Product savedProduct = this.productRepository.save(product);
        return ProductMapper.toResponse(savedProduct);
    }

    @Transactional
    @Override
    public ProductResponse deactivateProduct(Long productId) {
        Product product = this.getProductById(productId);

        product.deactivate();

        Product savedProduct = this.productRepository.save(product);
        return ProductMapper.toResponse(savedProduct);
    }
}