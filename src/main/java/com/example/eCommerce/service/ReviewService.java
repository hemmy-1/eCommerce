package com.example.eCommerce.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.eCommerce.Dtos.CreateReviewRequestDto;
import com.example.eCommerce.Dtos.ReviewResponseDto;
import com.example.eCommerce.entity.Product;
import com.example.eCommerce.entity.Review;
import com.example.eCommerce.entity.User;
import com.example.eCommerce.enums.OrderStatus;
import com.example.eCommerce.repository.OrderRepository;
import com.example.eCommerce.repository.ProductRepository;
import com.example.eCommerce.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public ReviewResponseDto createReview(UUID productId, User currentUser, CreateReviewRequestDto request) {
        // 1. Verify user purchased the product
        boolean hasPurchased = orderRepository.existsByCustomerIdAndProductIdAndStatus(
                currentUser.getId(), productId, OrderStatus.PAID);

        if (!hasPurchased) {
            throw new IllegalStateException("You can only review products you have purchased.");
        }

        // 2. Prevent duplicate reviews
        if (reviewRepository.existsByCustomerIdAndProductId(currentUser.getId(), productId)) {
            throw new IllegalStateException("You have already submitted a review for this product.");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // 3. Save review
        Review review = Review.builder()
                .customer(currentUser)
                .product(product)
                .rating(request.rating())
                .comment(request.comment())
                .build();

        Review savedReview = reviewRepository.save(review);

        return new ReviewResponseDto(
                savedReview.getId(),
                productId,
                currentUser.getNickName(),
                savedReview.getRating(),
                savedReview.getComment(),
                savedReview.getCreatedAt());
    }
}