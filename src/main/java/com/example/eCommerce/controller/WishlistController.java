package com.example.eCommerce.controller;

import com.example.eCommerce.Dtos.AddToWishlistRequestDto;
import com.example.eCommerce.Dtos.WishlistResponseDto;
import com.example.eCommerce.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    //done
    @GetMapping("/{customerId}")
    public ResponseEntity<List<WishlistResponseDto>> getWishlist(@PathVariable UUID customerId) {
        return ResponseEntity.ok(wishlistService.getWishlist(customerId));
    }

    //done
    @PostMapping("/{customerId}")
    public ResponseEntity<WishlistResponseDto> addToWishlist(
            @PathVariable UUID customerId,
            @RequestBody AddToWishlistRequestDto request) {
        WishlistResponseDto response = wishlistService.addToWishlist(customerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //done
    @DeleteMapping("/{customerId}/products/{productId}")
    public ResponseEntity<Void> removeFromWishlist(
            @PathVariable UUID customerId,
            @PathVariable UUID productId) {
        wishlistService.removeFromWishlist(customerId, productId);
        return ResponseEntity.noContent().build();
    }
}