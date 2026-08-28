package com.example.eCommerce.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.eCommerce.Dtos.CreateReviewRequestDto;
import com.example.eCommerce.Dtos.ReviewResponseDto;
import com.example.eCommerce.entity.User;
import com.example.eCommerce.service.ReviewService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products/{productId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(
            @PathVariable UUID productId,
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody CreateReviewRequestDto request) {

        return ResponseEntity.ok(reviewService.createReview(productId, currentUser, request));
    }
}