package com.uade.tpo.grupo7.marketplace.products.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.grupo7.marketplace.products.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByCode(String code);
    boolean existsByCodeAndIdNot(String code, Long id);
    boolean existsByParentId(Long parentId);
    List<Category> findAllByParentId(Long parentId);
    Optional<Category> findByCode(String code);
}
