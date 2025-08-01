package com.example.demo.application.services;

import com.example.demo.application.dto.CreateCategoryDto;
import com.example.demo.domain.models.Category;
import com.example.demo.domain.repositories.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepository categoryRepo;

    public CategoryService(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public Category create(CreateCategoryDto dto) {
        Category category = Category.builder()
                .name(dto.getName())
                .build();

        Category saved = categoryRepo.save(category);
        logger.info("Category created with ID: {}", saved.getId());
        return saved;
    }

    public List<Category> listAll(String name) {
        if (name != null && !name.isBlank()) {
            logger.info("Fetching categories filtered by name: {}", name);
            return categoryRepo.findByNameContainingIgnoreCase(name);
        }
        logger.info("Fetching all categories");
        return categoryRepo.findAll();
    }

    public Category update(Long id, CreateCategoryDto dto) {
        Category existing = categoryRepo.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Category with ID {} not found for update", id);
                    return new RuntimeException("Category not found");
                });

        existing.setName(dto.getName());
        Category updated = categoryRepo.save(existing);
        logger.info("Category updated with ID: {}", updated.getId());
        return updated;
    }

    public void delete(Long id) {
        Category existing = categoryRepo.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Category with ID {} not found for deletion", id);
                    return new RuntimeException("Category not found");
                });

        categoryRepo.delete(existing);
        logger.info("Category deleted with ID: {}", id);
    }

    public Category getById(Long id) {
        logger.info("Fetching category with ID: {}", id);
        return categoryRepo.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Category with ID {} not found", id);
                    return new RuntimeException("Category not found");
                });
    }
}
