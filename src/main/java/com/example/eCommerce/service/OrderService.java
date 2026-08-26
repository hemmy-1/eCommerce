package com.example.eCommerce.service;

import com.example.eCommerce.Dtos.CartItem;
import com.example.eCommerce.Dtos.CartResponseDto;
import com.example.eCommerce.Dtos.OrderItemResponseDto;
import com.example.eCommerce.Dtos.OrderResponseDto;
import com.example.eCommerce.entity.Order;
import com.example.eCommerce.entity.OrderItem;
import com.example.eCommerce.entity.Product;
import com.example.eCommerce.entity.User;
import com.example.eCommerce.enums.OrderStatus;
import com.example.eCommerce.enums.ProductStatus;
import com.example.eCommerce.repository.OrderRepository;
import com.example.eCommerce.repository.ProductRepository;
import com.example.eCommerce.repository.UserRepository;
import com.example.eCommerce.Exception.IllegalStateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartService cartService;

    private static final BigDecimal FIXED_SHIPPING_FEE = new BigDecimal("10.00");

    @Transactional
    public OrderResponseDto checkout(UUID customerId) {
        CartResponseDto cart = cartService.getCart(customerId);

        if (cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot process checkout: Cart is empty.");
        }

        User customer = userRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with ID: " + customerId));

        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.PENDING_PAYMENT);

        List<String> stockErrors = new ArrayList<>();
        BigDecimal computedSubtotal = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found: " + cartItem.getProductName()));

            if (product.getProductStatus() != ProductStatus.ACTIVE) {
                stockErrors.add("Product '" + product.getName() + "' is no longer active.");
                continue;
            }

            if (cartItem.getQuantity() > product.getStockQuantity()) {
                stockErrors.add("Product '" + product.getName() + "' has insufficient stock.");
                continue;
            }

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            computedSubtotal = computedSubtotal.add(itemTotal);

           
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setProductName(product.getName());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());

            order.addOrderItem(orderItem);
        }

        if (!stockErrors.isEmpty()) {
            throw new IllegalStateException("Checkout failed due to stock issues: " + String.join(" | ", stockErrors));
        }

        order.setSubtotal(computedSubtotal);
        order.setShippingFee(FIXED_SHIPPING_FEE);
        order.setTotalAmount(computedSubtotal.add(FIXED_SHIPPING_FEE));

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(customerId);

        return mapToDto(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> getCustomerOrders(UUID customerId) {
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customerId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));
        return mapToDto(order);
    }

    @Transactional
    public OrderResponseDto updateOrderStatus(UUID orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));

        if (!order.getStatus().canTransitionTo(newStatus)) {
            throw new IllegalStateException("Invalid status transition from " + order.getStatus() + " to " + newStatus);
        }

        order.setStatus(newStatus);
        Order updated = orderRepository.save(order);
        return mapToDto(updated);
    }

    private OrderResponseDto mapToDto(Order order) {
        List<OrderItemResponseDto> itemDtos = order.getItems().stream()
                .map(item -> new OrderItemResponseDto(
                        item.getProduct().getId(),
                        item.getProductName(),
                        item.getPrice(),
                        item.getQuantity(),
                        item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))))
                .toList();

        return new OrderResponseDto(
                order.getId(),
                order.getCustomer().getId(),
                order.getSubtotal(),
                order.getShippingFee(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt(),
                itemDtos);
    }
}