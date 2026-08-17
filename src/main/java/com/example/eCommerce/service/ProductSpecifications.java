package com.example.eCommerce.service;

import com.example.eCommerce.entity.Product;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;
import java.util.UUID;

public class ProductSpecifications {

    public static Specification<Product> hasKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty())
                return null;

            String pattern = "%" + keyword.trim().toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("name")), pattern),
                    cb.like(cb.lower(root.get("description")), pattern));
        };
    }

    public static Specification<Product> hasCategory(UUID categoryId) {
        return (root, query, cb) -> categoryId == null ? null : cb.equal(root.get("category").get("id"), categoryId);
    }

    
    public static Specification<Product> priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, cb) -> {
            if (minPrice != null && maxPrice != null) {
                return cb.between(root.get("price"), minPrice, maxPrice);
            } else if (minPrice != null) {
                return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
            } else if (maxPrice != null) {
                return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
            }
            return null;
        };
    }

    public static Specification<Product> isAvailable(Boolean available) {
        return (root, query, cb) -> {
            if (available == null)
                return null;
            return available
                    ? cb.greaterThan(root.get("stockQuantity"), 0)
                    : cb.equal(root.get("stockQuantity"), 0);
        };
    }
}