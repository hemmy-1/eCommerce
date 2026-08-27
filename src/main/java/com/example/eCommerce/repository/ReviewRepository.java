package com.example.eCommerce.repository;


import com.example.eCommerce.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    // Check if user has already reviewed the product
    boolean existsByCustomerIdAndProductId(UUID customerId, UUID productId);

    // Fetch paginated reviews for a specific product
    Page<Review> findByProductId(UUID productId, Pageable pageable);

    // Calculate average rating for a product
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Double findAverageRatingByProductId(@Param("productId") UUID productId);
}