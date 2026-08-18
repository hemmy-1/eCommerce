package com.example.eCommerce.Dtos;

import java.util.UUID;

import com.example.eCommerce.enums.CategoryStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponseDto {

    private UUID id;
    
    private String name;

    private String description;

    private CategoryStatus status;
}
