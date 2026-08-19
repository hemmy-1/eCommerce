package com.example.eCommerce.service;

import com.example.eCommerce.Dtos.PaymentRequestDto;
import com.example.eCommerce.Dtos.PaymentResponseDto;
import com.example.eCommerce.entity.Order;
import com.example.eCommerce.entity.OrderItem;
import com.example.eCommerce.entity.Payment;
import com.example.eCommerce.entity.Product;
import com.example.eCommerce.enums.OrderStatus;
import com.example.eCommerce.enums.PaymentStatus;
import com.example.eCommerce.repository.OrderRepository;
import com.example.eCommerce.repository.PaymentRepository;
import com.example.eCommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public PaymentResponseDto processPayment(PaymentRequestDto request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + request.getOrderId()));

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT) {
            throw new IllegalStateException("Order is not in PENDING_PAYMENT status.");
        }

        // FR-PAY-01: Create payment transaction record
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setTransactionReference("MOCK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        // FR-PAY-02: Simulate success vs failure
        if (request.isSimulateSuccess()) {
            // FR-PAY-03: Deduct stock ONLY when payment is confirmed successful
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
            // FR-PAY-04: Failed payment -> No stock deduction, order remains failed/unpaid
            payment.setStatus(PaymentStatus.FAILED);
            order.setStatus(OrderStatus.CANCELLED);
        }

        orderRepository.save(order);
        Payment savedPayment = paymentRepository.save(payment);

        return mapToDto(savedPayment);
    }

    private PaymentResponseDto mapToDto(Payment payment) {
        return new PaymentResponseDto(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getTransactionReference(),
                payment.getCreatedAt());
    }
}