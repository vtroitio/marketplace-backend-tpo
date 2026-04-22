package com.uade.tpo.grupo7.marketplace.products.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uade.tpo.grupo7.marketplace.products.entity.AttributeValue;

public interface AttributeValueRepository extends JpaRepository<AttributeValue, Long> {
    Optional<AttributeValue> findByCode(String code);
}
