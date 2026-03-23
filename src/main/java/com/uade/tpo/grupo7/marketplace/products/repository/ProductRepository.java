package com.uade.tpo.grupo7.marketplace.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.grupo7.marketplace.products.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {}