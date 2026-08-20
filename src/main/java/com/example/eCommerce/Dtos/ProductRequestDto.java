package com.example.eCommerce.Dtos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {

    private String name;

    private String description;

    private BigDecimal price;

    private List<String> imageUrls = new ArrayList<>();

    private Integer stockQuantity;

    private UUID categoryId;
    
}