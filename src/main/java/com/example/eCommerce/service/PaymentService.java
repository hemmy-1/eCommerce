package com.example.eCommerce.service;

import com.example.eCommerce.Dtos.PaymentResponseDto;
import com.example.eCommerce.entity.*;
import com.example.eCommerce.enums.OrderStatus;
import com.example.eCommerce.enums.PaymentStatus;
import com.example.eCommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${paystack.secret.key}")
    private String paystackSecretKey;

    @Transactional
    public PaymentResponseDto initializePayment(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new IllegalStateException("Order is not in PENDING_PAYMENT status.");
        }

        String reference = "TRX-" + UUID.randomUUID().toString();

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setTransactionReference(reference);
        Payment savedPayment = paymentRepository.save(payment);

        // Paystack API request body (Amount converted to kobo)
        Map<String, Object> body = new HashMap<>();
        body.put("email", order.getCustomer().getEmail());
        body.put("amount", order.getTotalAmount().multiply(new BigDecimal(100)).intValue());
        body.put("reference", reference);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(paystackSecretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        @SuppressWarnings("unchecked")
        Map<String, Object> response = restTemplate.postForObject(
                "https://api.paystack.co/transaction/initialize", entity, Map.class);

        if (response == null || response.get("data") == null) {
            throw new IllegalStateException("Failed to initialize payment transaction with Paystack");
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) response.get("data");
        String authorizationUrl = (String) data.get("authorization_url");

        return new PaymentResponseDto(
                savedPayment.getId(),
                order.getId(),
                savedPayment.getAmount(),
                savedPayment.getStatus(),
                reference,
                authorizationUrl,
                savedPayment.getCreatedAt());
    }
    
    @Transactional
    public void verifyAndFulfillPayment(String reference) {
        Payment payment = paymentRepository.findByTransactionReference(reference)
                .orElseThrow(() -> new IllegalArgumentException("Payment record not found: " + reference));

        // Idempotency check
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return;
        }

        // FR-PAY-05: Server-side verification directly against Paystack
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(paystackSecretKey);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                "https://api.paystack.co/transaction/verify/" + reference,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<Map<String, Object>>() {
                });

        Map<String, Object> responseBody = response.getBody();
        if (responseBody == null || responseBody.get("data") == null) {
            throw new IllegalStateException("Failed to verify transaction with Paystack");
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) responseBody.get("data");

        String status = (String) data.get("status");
        Number amountNumber = (Number) data.get("amount");
        BigDecimal verifiedAmount = BigDecimal.valueOf(amountNumber.longValue())
                .divide(BigDecimal.valueOf(100));

        Order order = payment.getOrder();

        if ("success".equalsIgnoreCase(status) && verifiedAmount.compareTo(payment.getAmount()) == 0) {
            for (OrderItem item : order.getItems()) {
                Product product = item.getProduct();
                if (product.getStockQuantity() < item.getQuantity()) {
                    throw new IllegalStateException("Insufficient stock for product: " + product.getName());
                }
                product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
                productRepository.save(product);
            }

            payment.setStatus(PaymentStatus.SUCCESS);
            order.setStatus(OrderStatus.PAID);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            order.setStatus(OrderStatus.CANCELLED);
        }

        orderRepository.save(order);
        paymentRepository.save(payment);
    }
}