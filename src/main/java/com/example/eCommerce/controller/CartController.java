package com.example.eCommerce.controller;

import com.example.eCommerce.Dtos.AddToCartRequestDto;
import com.example.eCommerce.Dtos.CartResponseDto;
import com.example.eCommerce.Dtos.UpdateCartItemRequestDto;
import com.example.eCommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    //worked
    @GetMapping("/{customerId}")
    public ResponseEntity<CartResponseDto> getCart(@PathVariable UUID customerId) {
        return ResponseEntity.ok(cartService.getCart(customerId));
    }

    // worked
    @PostMapping("/{customerId}/items")
    public ResponseEntity<CartResponseDto> addToCart(
            @PathVariable UUID customerId,
            @RequestBody AddToCartRequestDto request) {
        return ResponseEntity.ok(cartService.addToCart(customerId, request));
    }

    //worked
    @PutMapping("/{customerId}/items/{productId}")
    public ResponseEntity<CartResponseDto> updateQuantity(
            @PathVariable UUID customerId,
            @PathVariable UUID productId,
            @RequestBody UpdateCartItemRequestDto request) {
        return ResponseEntity.ok(cartService.updateQuantity(customerId, productId, request.getQuantity()));
    }

    //worked
    @DeleteMapping("/{customerId}/items/{productId}")
    public ResponseEntity<CartResponseDto> removeItem(
            @PathVariable UUID customerId,
            @PathVariable UUID productId) {
        return ResponseEntity.ok(cartService.removeItem(customerId, productId));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> clearCart(@PathVariable UUID customerId) {
        cartService.clearCart(customerId);
        return ResponseEntity.noContent().build();
    }
}