package com.example.eCommerce.Dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class AddToWishlistRequestDto {

    private UUID productId;
}