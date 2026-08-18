package com.example.eCommerce.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistResponseDto {

    private UUID wishlistId;
    private UUID productId;
    private String productName;
    private BigDecimal price;
    private LocalDateTime addedAt;
}