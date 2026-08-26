package com.example.eCommerce.service;

import com.example.eCommerce.Dtos.AddToWishlistRequestDto;
import com.example.eCommerce.Dtos.WishlistResponseDto;
import com.example.eCommerce.entity.Product;
import com.example.eCommerce.entity.User;
import com.example.eCommerce.entity.Wishlist;
import com.example.eCommerce.repository.ProductRepository;
import com.example.eCommerce.repository.UserRepository;
import com.example.eCommerce.repository.WishlistRepository;
import com.example.eCommerce.Exception.IllegalStateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public WishlistResponseDto addToWishlist(UUID customerId, AddToWishlistRequestDto request) {
        if (wishlistRepository.existsByCustomerIdAndProductId(customerId, request.getProductId())) {
            throw new IllegalStateException("Product is already present in customer's wishlist.");
        }

        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + customerId));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(
                        () -> new IllegalArgumentException("Product not found with ID: " + request.getProductId()));

        Wishlist wishlist = new Wishlist();
        wishlist.setCustomer(customer);
        wishlist.setProduct(product);
        wishlist.setCreatedAt(LocalDateTime.now());

        Wishlist savedWishlist = wishlistRepository.save(wishlist);
        return mapToDto(savedWishlist);
    }

    @Transactional
    public void removeFromWishlist(UUID customerId, UUID productId) {
        if (!wishlistRepository.existsByCustomerIdAndProductId(customerId, productId)) {
            throw new IllegalArgumentException("Product is not present in customer's wishlist.");
        }
        wishlistRepository.deleteByCustomerIdAndProductId(customerId, productId);
    }

    @Transactional(readOnly = true)
    public List<WishlistResponseDto> getWishlist(UUID customerId) {
        return wishlistRepository.findByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    private WishlistResponseDto mapToDto(Wishlist wishlist) {
        Product product = wishlist.getProduct();
        return new WishlistResponseDto(
                wishlist.getId(),
                product.getId(),
                product.getName(),
                product.getPrice(),
                wishlist.getCreatedAt());
    }
}