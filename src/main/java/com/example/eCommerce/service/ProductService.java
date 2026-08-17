package com.example.eCommerce.service;


import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.eCommerce.Dtos.ProductRequestDto;
import com.example.eCommerce.entity.Categories;
import com.example.eCommerce.entity.Product;
import com.example.eCommerce.repository.CategoryRepository;
import com.example.eCommerce.repository.ProductRepository;

@Service
public class ProductService {
    
    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository){
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public Product createProduct(ProductRequestDto request){
        productRepository.findByName(request.getName())
                        .orElseThrow(() -> new RuntimeException("this Product already exists"));

        Categories category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new RuntimeException("hello"));
        
                    
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setImageUrls(request.getImageUrls());
        product.setCategories(category);
        product.setPrice(request.getPrice());
        product.setProductStatus(product.getProductStatus());
        product.setStockQuantity(request.getStockQuantity());
        
        productRepository.save(product);
        return product;

    }

    public Page<Product> getFilteredProducts(
        UUID categoryId, 
        BigDecimal minPrice, 
        BigDecimal maxPrice, 
        Boolean available, 
        Pageable pageable) {

    Specification<Product> spec = Specification
            .where(ProductSpecifications.hasCategory(categoryId))
            .and(ProductSpecifications.priceBetween(minPrice, maxPrice))
            .and(ProductSpecifications.isAvailable(available));

    return productRepository.findAll(spec, pageable);
}
}
