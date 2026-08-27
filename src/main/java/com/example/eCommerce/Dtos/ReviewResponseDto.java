package com.example.eCommerce.Dtos;

import java.time.LocalDateTime;
import java.util.UUID;



public record ReviewResponseDto(
    UUID id,
    UUID productId,
    String customerName,
    Integer rating,
    String comment,
    LocalDateTime createdAt
) {} 
