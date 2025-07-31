package com.example.demo.infrastructure.persistence.repositories;

import com.example.demo.domain.models.Product;
import com.example.demo.domain.repositories.ProductRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final JpaProductRepository jpaRepo;

    public ProductRepositoryImpl(JpaProductRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Product save(Product product) {
        return jpaRepo.save(product);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpaRepo.findById(id);
    }

    @Override
    public List<Product> findAll() {
        return jpaRepo.findAll();
    }


    @Override
    public List<Product> findByNameContainingIgnoreCase(String name) {
        return jpaRepo.findByNameContainingIgnoreCase(name);
    }

    @Override
    public void delete(Product product) {
        jpaRepo.delete(product);
    }
}
