package com.example.demo.application.services;

import com.example.demo.application.dto.CreateProductDto;
import com.example.demo.domain.models.Product;
import com.example.demo.domain.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepo;

    public ProductService(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    public Product create(CreateProductDto dto) {
        Product product = Product.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .build();

        return productRepo.save(product);
    }

    public List<Product> listAll() {
        return productRepo.findAll();
    }
    public Product update(Long id, CreateProductDto dto) {
        Product existing = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        existing.setName(dto.getName());
        existing.setPrice(dto.getPrice());
        existing.setStock(dto.getStock());

        return productRepo.save(existing);
    }

    public void delete(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepo.delete(product);
    }

    public List<Product> listAll(String name) {
        if (name != null && !name.isBlank()) {
            return productRepo.findByNameContainingIgnoreCase(name);
        }
        return productRepo.findAll();
    }
    public Product getById(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product with ID " + id + " not found"));
    }


}
