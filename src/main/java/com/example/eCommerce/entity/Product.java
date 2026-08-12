package com.example.eCommerce.entity;

import java.util.UUID;

import com.example.eCommerce.enums.ProductStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue( strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;
    
    @Column( nullable = false )
    private String description;

    @Column( nullable = false )
    private int price;
        
    @Column( nullable = false )
    private String imageUrl;
    
    @Column( nullable = false )
    private int stockQuantity;
    
    @Column( nullable = false )
    private String category;

    private ProductStatus productStatus;
}
