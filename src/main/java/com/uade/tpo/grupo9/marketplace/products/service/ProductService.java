package com.uade.tpo.grupo9.marketplace.products.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.uade.tpo.grupo9.marketplace.products.dto.CreateProductDto;
import com.uade.tpo.grupo9.marketplace.products.entity.Product;
import com.uade.tpo.grupo9.marketplace.products.mapper.ProductMapper;
import com.uade.tpo.grupo9.marketplace.products.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productsRepository;

    public ProductService(ProductRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    /**
     * Retrieves all products stored in the repository.
     *
     * @return a list containing all {@link Product} entities currently
     * available
     */
    public Page<Product> getProducts(Pageable pageable) {
        return this.productsRepository.findAll(pageable);
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
        return this.productsRepository.findById(productId)
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
    public Product createProduct(CreateProductDto dto) {
        Product product = ProductMapper.toEntitiy(dto);
        return this.productsRepository.create(product);
    }
}
