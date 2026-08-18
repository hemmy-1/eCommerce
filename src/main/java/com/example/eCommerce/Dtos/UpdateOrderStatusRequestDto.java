package com.example.eCommerce.Dtos;

import com.example.eCommerce.enums.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderStatusRequestDto {
    private OrderStatus status;
}