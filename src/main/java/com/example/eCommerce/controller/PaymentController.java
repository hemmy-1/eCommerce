package com.example.eCommerce.controller;

import com.example.eCommerce.Dtos.PaymentResponseDto;
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

    // 1. Client initiates checkout and gets redirect URL
    @PostMapping("/initialize/{orderId}")
    public ResponseEntity<PaymentResponseDto> initializePayment(@PathVariable UUID orderId) {
        return ResponseEntity.ok(paymentService.initializePayment(orderId));
    }

    // 2. Paystack Webhook endpoint (Fulfills FR-PAY-05)
    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(@RequestBody Map<String, Object> payload) {
        String event = (String) payload.get("event");

        if ("charge.success".equals(event)) {
            Map<String, Object> data = (Map<String, Object>) payload.get("data");
            String reference = (String) data.get("reference");

            paymentService.verifyAndFulfillPayment(reference);
        }

        return ResponseEntity.ok().build();
    }
}