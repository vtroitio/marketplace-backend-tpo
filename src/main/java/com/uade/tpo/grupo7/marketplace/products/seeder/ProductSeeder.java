package com.uade.tpo.grupo7.marketplace.products.seeder;

import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.uade.tpo.grupo7.marketplace.products.entity.Category;
import com.uade.tpo.grupo7.marketplace.products.entity.Product;
import com.uade.tpo.grupo7.marketplace.products.repository.CategoryRepository;
import com.uade.tpo.grupo7.marketplace.products.repository.ProductRepository;

@Component
@Order(4)
public class ProductSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductSeeder(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() != 0) {
            return;
        }

        Category hombre = findCategory("HOMBRE");
        Category mujer = findCategory("MUJER");
        Category hombreRemera = findCategory("HOMBRE_REMERA");
        Category hombreShort = findCategory("HOMBRE_SHORT");
        Category mujerRemera = findCategory("MUJER_REMERA");
        Category mujerPantalon = findCategory("MUJER_PANTALON");
        Category mujerTop = findCategory("MUJER_TOP");

        productRepository.saveAll(List.of(
                Product.builder()
                        .name("Remera Naruto Akatsuki")
                        .price(19999.0)
                        .description("Remera de algodon para hombre con estampa de Naruto")
                        .categories(Set.of(hombre, hombreRemera))
                        .build(),
                Product.builder()
                        .name("Short Dragon Ball Goku")
                        .price(15999.0)
                        .description("Short para hombre con diseno inspirado en Dragon Ball")
                        .categories(Set.of(hombre, hombreShort))
                        .build(),
                Product.builder()
                        .name("Pantalon Pokemon Retro")
                        .price(24999.0)
                        .description("Pantalon para mujer con diseno retro de Pokemon")
                        .categories(Set.of(mujer, mujerPantalon))
                        .build(),
                Product.builder()
                        .name("Remera One Piece Luffy")
                        .price(20999.0)
                        .description("Remera para mujer con estampa de One Piece")
                        .categories(Set.of(mujer, mujerRemera))
                        .build(),
                Product.builder()
                        .name("Top Sailor Scouts")
                        .price(18999.0)
                        .description("Top para mujer inspirado en Sailor Moon")
                        .categories(Set.of(mujer, mujerTop))
                        .build()
        ));
    }

    private Category findCategory(String code) {
        return categoryRepository.findByCode(code)
                .orElseThrow(() -> new IllegalStateException("Category not found: " + code));
    }
}
