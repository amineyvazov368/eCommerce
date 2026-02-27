package org.example.ecommers.repository;

import org.example.ecommers.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    boolean existsByName(String name);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByCategoryNameContainingIgnoreCase(String categoryName);

    List<Product> findByPriceBetween(BigDecimal lower, BigDecimal higher);

    boolean existsByCategoryId(Long categoryId);
}
