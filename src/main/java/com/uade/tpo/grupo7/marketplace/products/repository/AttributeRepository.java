package com.uade.tpo.grupo7.marketplace.products.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.grupo7.marketplace.products.entity.Attribute;

public interface AttributeRepository extends JpaRepository<Attribute, Long> {

    @EntityGraph(attributePaths = "values")
    List<Attribute> findAllByOrderByIdAsc();

    @EntityGraph(attributePaths = "values")
    Optional<Attribute> findWithValuesById(Long id);

    Optional<Attribute> findByCode(String code);
}
