package com.example.eCommerce.enums;

import java.util.Set;

public enum OrderStatus {
    PENDING_PAYMENT,
    PAID,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    REFUNDED;

    public boolean canTransitionTo(OrderStatus nextStatus) {
        return switch (this) {
            case PENDING_PAYMENT -> Set.of(PAID, CANCELLED).contains(nextStatus);
            case PAID -> Set.of(PROCESSING, REFUNDED, CANCELLED).contains(nextStatus);
            case PROCESSING -> Set.of(SHIPPED, CANCELLED, REFUNDED).contains(nextStatus);
            case SHIPPED -> Set.of(DELIVERED, REFUNDED).contains(nextStatus);
            case DELIVERED, CANCELLED, REFUNDED -> false; 
        };
    }
}