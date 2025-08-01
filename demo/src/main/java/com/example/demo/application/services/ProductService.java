package com.example.demo.application.services;

import com.example.demo.application.dto.CreateProductDto;
import com.example.demo.domain.models.Category;
import com.example.demo.domain.models.Product;
import com.example.demo.domain.repositories.CategoryRepository;
import com.example.demo.domain.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepo;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private CategoryRepository categoryRepo;

    public ProductService(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    public Product create(CreateProductDto dto) {
        logger.info("Start creating product with name: {}", dto.getName());

        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> {
                    logger.error("Category not found with ID: {}", dto.getCategoryId());
                    return new RuntimeException("Category not found");
                });

        Product product = Product.builder()
                .name(dto.getName())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .category(category)
                .build();

        Product saved = productRepo.save(product);
        logger.info("Product created with ID: {}", saved.getId());
        return saved;
    }

    public List<Product> listAll() {
        logger.info("Fetching all products");
        return productRepo.findAll();
    }

    public Product update(Long id, CreateProductDto dto) {
        logger.info("Updating product with ID: {}", id);

        Product existing = productRepo.findById(id)
                .orElseThrow(() -> {
                    logger.error("Product not found with ID: {}", id);
                    return new RuntimeException("Product not found");
                });

        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> {
                    logger.error("Category not found with ID: {}", dto.getCategoryId());
                    return new RuntimeException("Category not found");
                });

        existing.setCategory(category);
        existing.setName(dto.getName());
        existing.setPrice(dto.getPrice());
        existing.setStock(dto.getStock());

        Product updated = productRepo.save(existing);
        logger.info("Product updated with ID: {}", updated.getId());
        return updated;
    }

    public void delete(Long id) {
        logger.info("Deleting product with ID: {}", id);

        Product product = productRepo.findById(id)
                .orElseThrow(() -> {
                    logger.error("Product not found with ID: {}", id);
                    return new RuntimeException("Product not found");
                });

        productRepo.delete(product);
        logger.info("Product deleted with ID: {}", id);
    }

    public List<Product> listAll(String name) {
        if (name != null && !name.isBlank()) {
            logger.info("Searching products with name containing: {}", name);
            return productRepo.findByNameContainingIgnoreCase(name);
        }
        logger.info("Fetching all products (no filter)");
        return productRepo.findAll();
    }

    public Product getById(Long id) {
        logger.info("Fetching product with ID: {}", id);
        return productRepo.findById(id)
                .orElseThrow(() -> {
                    logger.error("Product not found with ID: {}", id);
                    return new RuntimeException("Product with ID " + id + " not found");
                });
    }
}
