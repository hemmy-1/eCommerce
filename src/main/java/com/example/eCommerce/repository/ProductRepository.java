package com.example.eCommerce.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.eCommerce.entity.Product;
import com.example.eCommerce.enums.ProductStatus;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    Optional<String> findByName(String name);

    List<Product> findByProductStatus(ProductStatus productStatus);
    
}
