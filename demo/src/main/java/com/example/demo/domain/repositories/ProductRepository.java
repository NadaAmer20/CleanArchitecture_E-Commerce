package com.example.demo.domain.repositories;

import com.example.demo.domain.models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(Long id);
    List<Product> findAll();// Optional to avoid null issues
    List<Product> findByNameContainingIgnoreCase(String name);
    void delete(Product product);

}
