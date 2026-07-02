package com.uade.tpo.grupo7.marketplace.products.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.uade.tpo.grupo7.marketplace.products.entity.Category;
import com.uade.tpo.grupo7.marketplace.products.repository.CategoryRepository;

@Component
@Order(3)
public class CategorySeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public CategorySeeder(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        if (categoryRepository.count() != 0) {
            return;
        }

        Category hombre = categoryRepository.save(Category.builder().name("Hombre").code("HOMBRE").build());
        Category mujer = categoryRepository.save(Category.builder().name("Mujer").code("MUJER").build());

        categoryRepository.save(Category.builder().name("Remera de Hombre").code("HOMBRE_REMERA").parent(hombre).build());
        categoryRepository.save(Category.builder().name("Pantalon de Hombre").code("HOMBRE_PANTALON").parent(hombre).build());
        categoryRepository.save(Category.builder().name("Short de Hombre").code("HOMBRE_SHORT").parent(hombre).build());

        categoryRepository.save(Category.builder().name("Remera de Mujer").code("MUJER_REMERA").parent(mujer).build());
        categoryRepository.save(Category.builder().name("Pantalon de Mujer").code("MUJER_PANTALON").parent(mujer).build());
        categoryRepository.save(Category.builder().name("Short de Mujer").code("MUJER_SHORT").parent(mujer).build());
        categoryRepository.save(Category.builder().name("Top de Mujer").code("MUJER_TOP").parent(mujer).build());
    }
}
