package com.example.eCommerce.Dtos;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentRequestDto {

    
    private UUID orderId;

    // FR-PAY-02: Simulated payment outcome flag
    private boolean simulateSuccess = true;
}