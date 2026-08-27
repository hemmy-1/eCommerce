package com.example.eCommerce.controller;

import com.example.eCommerce.Dtos.OrderResponseDto;
import com.example.eCommerce.Dtos.UpdateOrderStatusRequestDto;
import com.example.eCommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout/{customerId}")
    public ResponseEntity<OrderResponseDto> checkout(@PathVariable UUID customerId) {
        OrderResponseDto response = orderService.checkout(customerId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponseDto>> getCustomerOrders(@PathVariable UUID customerId) {
        return ResponseEntity.ok(orderService.getCustomerOrders(customerId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @PatchMapping("/{orderId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
            @PathVariable UUID orderId,
            @RequestBody UpdateOrderStatusRequestDto request) {
        OrderResponseDto response = orderService.updateOrderStatus(orderId, request.getStatus());
        return ResponseEntity.ok(response);
    }
}