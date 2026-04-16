package com.uade.tpo.grupo7.marketplace.products.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
import com.uade.tpo.grupo7.marketplace.products.entity.Product;
import com.uade.tpo.grupo7.marketplace.products.entity.ProductImage;
import com.uade.tpo.grupo7.marketplace.products.mapper.ProductMapper;
import com.uade.tpo.grupo7.marketplace.products.repository.ProductImageRepository;
import com.uade.tpo.grupo7.marketplace.products.repository.ProductRepository;
import com.uade.tpo.grupo7.marketplace.users.entity.User;

@Service
public class ProductService {

    private static final int MAX_IMAGES = 10;

    @Value("${file.upload-dir}")
    private String uploadDir;


    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    public ProductService(ProductRepository productRepository, ProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
    }

    /**
    * Retrieves a paginated list of products from the repository.
    *
    * @param pageable the pagination information (page number, page size, sorting)
    * @return a page of products matching the pagination criteria
    */
    public Page<Product> getProducts(Pageable pageable) {
        return this.productRepository.findAll(pageable);
    }

    /**
     * Retrieves a single product by its identifier.
     *
     * <p>
     * If the product does not exist, a {@link ResponseStatusException} with
     * HTTP status {@code 404 NOT_FOUND} is thrown.</p>
     *
     * @param productId the unique identifier of the product to retrieve
     * @return the {@link Product} matching the provided identifier
     * @throws ResponseStatusException if no product with the given id exists
     */
    public Product getProductById(int productId) throws ResponseStatusException {
        return this.productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Product not found"
                ));
    }

    /**
     * Creates a new product from the provided DTO and stores it in the
     * repository.
     *
     * <p>
     * The DTO is first converted into a {@link Product} entity using
     * {@link ProductMapper}, then persisted via the repository layer.</p>
     *
     * @param dto the data transfer object containing the information required
     * to create a new product
     * @return the newly created {@link Product} entity
     */
    public Product createProduct(CreateProductRequest dto) {
        Product product = ProductMapper.toEntitiy(dto);
        return this.productRepository.save(product);
    }

    /**
     * Updates an existing product with the provided information.
     *
     * @param productId the ID of the product to update
     * @param dto the {@link UpdateProductRequest} containing the updated product information
     * @return the updated {@link Product} object after saving to the repository
     * @throws ResponseStatusException if the product with the specified ID is not found
     */
    public Product updateProduct(int productId, UpdateProductRequest dto) throws ResponseStatusException {
        Product product = this.getProductById(productId);

        if (dto.name() != null) {
            product.setName(dto.name());
        }

        if (dto.price() != null) {
            product.setPrice(dto.price());
        }

        return this.productRepository.save(product);
    }

    /**
     * Deletes a product from the repository by its ID.
     * 
     * @param productId the ID of the product to delete
     * @throws ResponseStatusException if the product with the specified ID is not found
     */
    public void deleteProduct(int productId) throws ResponseStatusException {
        this.getProductById(productId);
        this.productRepository.deleteById(productId);
    }

    public List<ProductImage> uploadProductImages(int productId, List<MultipartFile> files) {
        final int currentImages = this.productImageRepository.countByProductId(productId);

        if (currentImages + files.size() > MAX_IMAGES) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, 
                "Cannot upload more than " + MAX_IMAGES + " images for a product.");
        }

        Product product = this.getProductById(productId);

        int order = currentImages;
        for (MultipartFile file : files) {
            try {
                String filePath = this.saveFile(file);
                ProductImage productImage = ProductImage.builder()
                    .product(product)
                    .order(order)
                    .path(filePath)
                    .build();
                this.productImageRepository.save(productImage);
                order++;
            } catch (IOException e) {
                throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, 
                    "Error saving file: " + file.getOriginalFilename());
            }
        }

        return this.productImageRepository.findAllByProductId(productId);
    }

    private String saveFile(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalName = file.getOriginalFilename();
        String fileName = UUID.randomUUID() + "_" + originalName;
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        return filePath.toString();
    }
}