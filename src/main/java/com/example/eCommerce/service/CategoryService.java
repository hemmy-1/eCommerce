package com.example.eCommerce.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.eCommerce.Dtos.CategoryRequestDto;
import com.example.eCommerce.Dtos.CategoryResponseDto;
import com.example.eCommerce.entity.Categories;
import com.example.eCommerce.enums.CategoryStatus;
import com.example.eCommerce.repository.CategoryRepository;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponseDto createCategory(CategoryRequestDto request) {
        if (categoryRepository.findByName(request.getName()).isPresent()) {
            throw new RuntimeException("Category with name '" + request.getName() + "' already exists");
        }

        Categories category = new Categories();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setStatus(CategoryStatus.ACTIVE);

        Categories saved = categoryRepository.save(category);

        return new CategoryResponseDto(
                saved.getId(),
                saved.getName(),
                saved.getDescription(),
                saved.getStatus());

    }


    public CategoryResponseDto updateCategory(UUID id, CategoryRequestDto request) {
        Categories category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));

        category.setName(request.getName());
        category.setDescription(request.getDescription());


        Categories updated = categoryRepository.save(category);
        return mapToDto(updated);
    }

    public CategoryResponseDto deactivateCategory(UUID id) {
        Categories category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));

        category.setStatus(CategoryStatus.INACTIVE);

        Categories updated = categoryRepository.save(category);
        return mapToDto(updated);
    }

    public List<CategoryResponseDto> getActiveCategories() {
        return categoryRepository.findByStatus(CategoryStatus.ACTIVE)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private CategoryResponseDto mapToDto(Categories category) {
        return new CategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getStatus()
        );
    }

}
