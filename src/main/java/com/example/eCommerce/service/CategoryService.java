package com.example.eCommerce.service;


import org.springframework.stereotype.Service;

import com.example.eCommerce.Dtos.CategoryRequestDto;
import com.example.eCommerce.entity.Categories;
import com.example.eCommerce.repository.CategoryRepository;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    public Categories createCategory(CategoryRequestDto request ){

        categoryRepository.findByname(request.getName()).orElseThrow(() -> new RuntimeException("this category already exists"));

        Categories categories = new Categories();

        categories.setName(request.getName());
        categories.setDescription(request.getDescription());

        categoryRepository.save(categories);

        return categories;

    }
    
}
