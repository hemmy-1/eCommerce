package com.example.eCommerce.Dtos;

import com.example.eCommerce.enums.PaymentStatus;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


public record PaymentResponseDto(
        UUID paymentId,
        UUID orderId,
        BigDecimal amount,
        PaymentStatus status,
        String transactionReference,
        String paymentUrl,
        LocalDateTime createdAt) {
}