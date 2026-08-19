package com.example.eCommerce.Dtos;

import com.example.eCommerce.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {

    private UUID paymentId;
    private UUID orderId;
    private BigDecimal amount;
    private PaymentStatus status;
    private String transactionReference;
    private LocalDateTime createdAt;
}