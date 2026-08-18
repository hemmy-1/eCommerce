package com.example.eCommerce.Dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class AddToCartRequestDto {

    private UUID productId;

   
    private Integer quantity;
}