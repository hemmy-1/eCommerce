package com.example.eCommerce.repository;

import com.example.eCommerce.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, UUID> {

    List<Wishlist> findByCustomerIdOrderByCreatedAtDesc(UUID customerId);

    boolean existsByCustomerIdAndProductId(UUID customerId, UUID productId);

    Optional<Wishlist> findByCustomerIdAndProductId(UUID customerId, UUID productId);

    void deleteByCustomerIdAndProductId(UUID customerId, UUID productId);
}