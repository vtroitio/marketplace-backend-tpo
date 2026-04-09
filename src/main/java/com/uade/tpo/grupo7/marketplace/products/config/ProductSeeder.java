package com.uade.tpo.grupo7.marketplace.products.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.uade.tpo.grupo7.marketplace.products.entity.Product;
import com.uade.tpo.grupo7.marketplace.products.repository.ProductRepository;

@Component
public class ProductSeeder implements CommandLineRunner {

   private final ProductRepository productRepository;

    public ProductSeeder(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (this.productRepository.count() != 0) {
            return;
        }

        for (int i = 1; i <= 25; i++) {
            this.productRepository.save(
                Product.builder()
                    .name("Remera " + i)
                    .price(10.0 * i)
                    .build()
                );
			}
    }

}
