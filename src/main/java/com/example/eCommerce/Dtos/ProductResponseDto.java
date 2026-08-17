package com.example.eCommerce.Dtos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.eCommerce.enums.ProductStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {

    private UUID id;

    private String name;

    private String description;

    private BigDecimal price;

    private List<String> imageUrls = new ArrayList<>();

    private int stockQuantity;

    private String categoryName;

    private ProductStatus productStatus;
}