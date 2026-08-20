package com.example.eCommerce.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.eCommerce.Dtos.ProductRequestDto;
import com.example.eCommerce.Dtos.ProductResponseDto;
import com.example.eCommerce.entity.Categories;
import com.example.eCommerce.entity.Product;
import com.example.eCommerce.enums.ProductStatus;
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

    // create product
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
        product.setProductStatus(ProductStatus.ACTIVE);
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

    public List<ProductResponseDto> allProduct() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .map(product -> new ProductResponseDto(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getImageUrls(),
                        product.getStockQuantity(),
                        product.getCategories().getName(),
                        product.getProductStatus()))
                .toList();
    }

    public List<ProductResponseDto> allActiveProducts() {

        List<Product> products = productRepository.findByProductStatus(ProductStatus.ACTIVE);

        return products.stream()
                .map(product -> new ProductResponseDto(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getImageUrls(),
                        product.getStockQuantity(),
                        product.getCategories().getName(),
                        product.getProductStatus()))
                .toList();
    }

    public ProductResponseDto activeProductDetails(String name) {
        Product product = productRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("this product is not avialable"));

        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrls(),
                product.getStockQuantity(),
                product.getCategories().getName(),
                product.getProductStatus());
    }

    public ProductResponseDto deactivateProduct(ProductRequestDto request) {
        Product product = productRepository.findByName(request.getName())
                .orElseThrow(() -> new RuntimeException("product not found with name:  " + request.getName()));

        product.setProductStatus(ProductStatus.DEACTIVATE);

        Product updatedProduct = productRepository.save(product);

        return new ProductResponseDto(
                updatedProduct.getId(),
                updatedProduct.getName(),
                updatedProduct.getDescription(),
                updatedProduct.getPrice(),
                updatedProduct.getImageUrls(),
                updatedProduct.getStockQuantity(),
                updatedProduct.getCategories().getName(),
                updatedProduct.getProductStatus());
    }

    // filterProducts
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
