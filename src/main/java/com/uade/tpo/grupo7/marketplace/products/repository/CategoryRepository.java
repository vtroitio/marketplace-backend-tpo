package com.uade.tpo.grupo7.marketplace.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.uade.tpo.grupo7.marketplace.products.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}