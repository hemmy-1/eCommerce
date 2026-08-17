package com.example.eCommerce.service;

import java.math.BigDecimal;
import java.util.Locale.Category;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.eCommerce.Dtos.ProductRequestDto;
import com.example.eCommerce.Dtos.ProductResponseDto;
import com.example.eCommerce.entity.Categories;
import com.example.eCommerce.entity.Product;
import com.example.eCommerce.repository.CategoryRepository;
import com.example.eCommerce.repository.ProductRepository;

@Service
public class ProductService {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductResponseDto createProduct(ProductRequestDto request) {
    if (productRepository.findByName(request.getName()).isPresent()) {
        throw new RuntimeException("A product with this name already exists");
    }

    Categories category = categoryRepository.findById(request.getCategoryId())
            .orElseThrow(() -> new RuntimeException("Category not found with ID: " + request.getCategoryId()));

    Product product = new Product();
    product.setName(request.getName());
    product.setDescription(request.getDescription());
    product.setImageUrls(request.getImageUrls());
    product.setCategories(category);
    product.setPrice(request.getPrice());
    product.setProductStatus(request.getProductStatus()); 
    product.setStockQuantity(request.getStockQuantity());

    Product savedProduct = productRepository.save(product);

    return new ProductResponseDto(
            savedProduct.getId(),
            savedProduct.getName(),
            savedProduct.getDescription(),
            savedProduct.getPrice(),
            savedProduct.getImageUrls(),
            savedProduct.getStockQuantity(),
            savedProduct.getCategories().getName(),
            savedProduct.getProductStatus());
}




    public Page<Product> getFilteredProducts(
            String keyword,
            UUID categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean available,
            Pageable pageable) {

        Specification<Product> spec = Specification
                .where(ProductSpecifications.hasKeyword(keyword))
                .and(ProductSpecifications.hasCategory(categoryId))
                .and(ProductSpecifications.priceBetween(minPrice, maxPrice))
                .and(ProductSpecifications.isAvailable(available));

        return productRepository.findAll(spec, pageable);
    }
}
