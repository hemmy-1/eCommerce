package com.example.eCommerce.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.eCommerce.entity.Categories;
import com.example.eCommerce.enums.CategoryStatus;

public interface CategoryRepository extends JpaRepository<Categories, UUID> {


    Optional<Categories> findByName(String name);

    List<Categories> findByStatus(CategoryStatus status);
    
}
