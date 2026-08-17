package com.example.eCommerce.repository;

import java.util.Optional;
import java.util.UUID;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.eCommerce.entity.Categories;

public interface CategoryRepository extends JpaRepository<Categories, UUID> {

    Optional<String> findByname(String name);
    
}
