package com.example.eCommerce.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.eCommerce.Dtos.ProductRequestDto;
import com.example.eCommerce.Dtos.ProductResponseDto;
import com.example.eCommerce.entity.Product;
import com.example.eCommerce.service.ProductService;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //done
    @PostMapping("create")
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto request) {
        ProductResponseDto response = productService.createProduct(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //done
    @GetMapping("all")
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> response = productService.allProduct();
        return ResponseEntity.ok(response);
    }

    //done
    @GetMapping("activeProducts")
    public ResponseEntity<List<ProductResponseDto>> getAllActiveProducts() {
        List<ProductResponseDto> response = productService.allActiveProducts();
        return ResponseEntity.ok(response);
    }

    //done
    @PatchMapping("/deactivate/{name}")
    public ResponseEntity<ProductResponseDto> deactivateProduct(@PathVariable String name) {
        ProductResponseDto response = productService.deactivateProduct(name);
        return ResponseEntity.ok(response);
    }

    //done
    @GetMapping("/detail/{name}")
    public ResponseEntity<ProductResponseDto> getActiveProductDetail(@PathVariable String name) {
        ProductResponseDto response = productService.activeProductDetails(name);
        return ResponseEntity.ok(response);
    }

    //done
    @PostMapping("search")
    public ResponseEntity<Page<Product>> listProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean available,
            Pageable pageable) {

        Page<Product> products = productService.getFilteredProducts(
                keyword, categoryId, minPrice, maxPrice, available, pageable);
        return ResponseEntity.ok(products);
    }
}
