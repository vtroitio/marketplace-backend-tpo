package com.uade.tpo.grupo9.marketplace.products.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

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
        products.add(Product.builder()
                .id(6)
                .name("Pantalón Negro")
                .price(25.0)
                .build()
        );

        products.add(Product.builder()
                .id(7)
                .name("Pantalón Jeans Azul")
                .price(40.0)
                .build()
        );

        products.add(Product.builder()
                .id(8)
                .name("Camisa Blanca")
                .price(30.0)
                .build()
        );

        products.add(Product.builder()
                .id(9)
                .name("Camisa Cuadros")
                .price(35.0)
                .build()
        );

        products.add(Product.builder()
                .id(10)
                .name("Buzo Gris")
                .price(45.0)
                .build()
        );

        products.add(Product.builder()
                .id(11)
                .name("Buzo Negro")
                .price(50.0)
                .build()
        );

        products.add(Product.builder()
                .id(12)
                .name("Campera Cuero")
                .price(120.0)
                .build()
        );

        products.add(Product.builder()
                .id(13)
                .name("Campera Inflable")
                .price(90.0)
                .build()
        );

        products.add(Product.builder()
                .id(14)
                .name("Short Deportivo")
                .price(20.0)
                .build()
        );

        products.add(Product.builder()
                .id(15)
                .name("Zapatillas Running")
                .price(80.0)
                .build()
        );

        products.add(Product.builder()
                .id(16)
                .name("Zapatillas Urbanas")
                .price(70.0)
                .build()
        );

        products.add(Product.builder()
                .id(17)
                .name("Gorra Negra")
                .price(15.0)
                .build()
        );

        products.add(Product.builder()
                .id(18)
                .name("Gorra Blanca")
                .price(15.0)
                .build()
        );

        products.add(Product.builder()
                .id(19)
                .name("Medias Deportivas")
                .price(10.0)
                .build()
        );

        products.add(Product.builder()
                .id(20)
                .name("Cinturón Cuero")
                .price(25.0)
                .build()
        );

        products.add(Product.builder()
                .id(21)
                .name("Camisa Denim")
                .price(45.0)
                .build()
        );

        products.add(Product.builder()
                .id(22)
                .name("Sweater Lana")
                .price(55.0)
                .build()
        );

        products.add(Product.builder()
                .id(23)
                .name("Chomba Azul")
                .price(28.0)
                .build()
        );

        products.add(Product.builder()
                .id(24)
                .name("Chomba Roja")
                .price(28.0)
                .build()
        );

        products.add(Product.builder()
                .id(25)
                .name("Mochila Urbana")
                .price(60.0)
                .build()
        );
    }

    /**
     * Retrieves all products currently stored in the repository.
     *
     * @return a list containing all {@link Product} entities
     */
    public Page<Product> findAll(Pageable pageable) {

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), products.size());

        List<Product> content;

        if (start >= products.size()) {
            content = List.of();
        } else {
            content = products.subList(start, end);
        }

        return new PageImpl<>(content, pageable, products.size());
    }

    /**
     * Searches for a product by its identifier.
     *
     * @param id the unique identifier of the product to search for
     * @return an {@link Optional} containing the matching {@link Product} if
     * found, or an empty {@link Optional} otherwise
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

    public void deleteById(int productId) {
        this.products.removeIf(product -> product.getId() == productId);
    }

}
