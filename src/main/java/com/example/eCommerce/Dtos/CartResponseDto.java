package com.example.eCommerce.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDto {

    private UUID customerId;
    private List<CartItem> items = new ArrayList<>();
    private BigDecimal cartSubtotal;
}