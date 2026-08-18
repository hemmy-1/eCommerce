package com.example.eCommerce.service;

import com.example.eCommerce.Dtos.AddToCartRequestDto;
import com.example.eCommerce.Dtos.CartItem;
import com.example.eCommerce.Dtos.CartResponseDto;
import com.example.eCommerce.entity.Product;
import com.example.eCommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ProductRepository productRepository;

    private static final Duration CART_TTL = Duration.ofDays(1);

    private String getCartKey(UUID customerId) {
        return "cart:" + customerId.toString();
    }

    public CartResponseDto addToCart(UUID customerId, AddToCartRequestDto request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + request.getProductId()));

        String key = getCartKey(customerId);
        String hashKey = product.getId().toString();

        CartItem existingItem = (CartItem) redisTemplate.opsForHash().get(key, hashKey);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            redisTemplate.opsForHash().put(key, hashKey, existingItem);
        } else {
            CartItem newItem = new CartItem(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    request.getQuantity());
            redisTemplate.opsForHash().put(key, hashKey, newItem);
        }


        redisTemplate.expire(key, CART_TTL);

        return getCart(customerId);
    }


    public CartResponseDto updateQuantity(UUID customerId, UUID productId, Integer newQuantity) {
        String key = getCartKey(customerId);
        String hashKey = productId.toString();

        CartItem item = (CartItem) redisTemplate.opsForHash().get(key, hashKey);
        if (item == null) {
            throw new RuntimeException("Item not found in cart");
        }

        item.setQuantity(newQuantity);
        redisTemplate.opsForHash().put(key, hashKey, item);


        redisTemplate.expire(key, CART_TTL);

        return getCart(customerId);
    }


    public CartResponseDto removeItem(UUID customerId, UUID productId) {
        String key = getCartKey(customerId);
        redisTemplate.opsForHash().delete(key, productId.toString());


        redisTemplate.expire(key, CART_TTL);

        return getCart(customerId);
    }


    public CartResponseDto getCart(UUID customerId) {
        String key = getCartKey(customerId);
        Map<Object, Object> rawItems = redisTemplate.opsForHash().entries(key);

        List<CartItem> items = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (Object value : rawItems.values()) {
            CartItem item = (CartItem) value;
            items.add(item);
            subtotal = subtotal.add(item.getItemSubtotal());
        }

        return new CartResponseDto(customerId, items, subtotal);
    }


    public void clearCart(UUID customerId) {
        String key = getCartKey(customerId);
        redisTemplate.delete(key);
    }
}