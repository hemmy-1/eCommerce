package com.example.eCommerce.repository;

import com.example.eCommerce.entity.Order;
import com.example.eCommerce.enums.OrderStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    
    List<Order> findByCustomerIdOrderByCreatedAtDesc(UUID customerId);

    @Query("""
        SELECT COUNT(o) > 0 FROM Order o 
        JOIN o.items item 
        WHERE o.customer.id = :customerId 
          AND item.product.id = :productId 
          AND o.status = :status
    """)
    boolean existsByCustomerIdAndProductIdAndStatus(
            @Param("customerId") UUID customerId,
            @Param("productId") UUID productId,
            @Param("status") OrderStatus status
    );
    
}