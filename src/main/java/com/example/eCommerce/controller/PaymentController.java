package com.example.eCommerce.controller;

import com.example.eCommerce.Dtos.PaymentResponseDto;
import com.example.eCommerce.Dtos.PaystackWebhookDto;
import com.example.eCommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/initialize/{orderId}")
    public ResponseEntity<PaymentResponseDto> initializePayment(@PathVariable UUID orderId) {
        return ResponseEntity.ok(paymentService.initializePayment(orderId));
    }

    @PostMapping("/webhook")
public ResponseEntity<Void> handleWebhook(@RequestBody PaystackWebhookDto payload) {
    if ("charge.success".equalsIgnoreCase(payload.getEvent()) && payload.getData() != null) {
        String reference = payload.getData().getReference();
        
        paymentService.verifyAndFulfillPayment(reference);
    }

    return ResponseEntity.ok().build();
}
}