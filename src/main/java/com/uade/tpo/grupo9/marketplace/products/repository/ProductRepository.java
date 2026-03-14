package com.uade.tpo.grupo9.marketplace.products.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.uade.tpo.grupo9.marketplace.products.entity.Product;

@Repository
public class ProductRepository {
    private final ArrayList<Product> products = new ArrayList<Product>();

    public ProductRepository() {
        products.add(Product.builder()
            .id(1)
            .name("Remera Negra")
            .price(10.50)
            .build()
        );
        products.add(Product.builder()
            .id(2)
            .name("Remera Blanca")
            .price(10.50)
            .build()
        );
        products.add(Product.builder()
            .id(3)
            .name("Remera Gris")
            .price(10.50)
            .build()
        );
        products.add(Product.builder()
            .id(4)
            .name("Remera Roja")
            .price(15.0)
            .build()
        );
        products.add(Product.builder()
            .id(5)
            .name("Remera Azúl")
            .price(15.0)
            .build()
        );
    }

    /**
     * Retrieves all products currently stored in the repository.
     *
     * @return a list containing all {@link Product} entities
     */
    public List<Product> findAll() {
        return this.products;
    }

    /**
     * Searches for a product by its identifier.
     *
     * @param id the unique identifier of the product to search for
     * @return an {@link Optional} containing the matching {@link Product}
     *         if found, or an empty {@link Optional} otherwise
     */
    public Optional<Product> findById(int id) {
        return this.products.stream()
                    .filter(product -> product.getId() == id)
                    .findFirst();
    }

    /**
     * Persists a new product in the repository.
     *
     * @param product the {@link Product} entity to be stored
     * @return the created {@link Product} with the generated identifier
     */
    public Product create(Product product) {
        int nextId = this.products.size() + 1;

        product.setId(nextId);
        this.products.add(product);

        return product;
    }

}
