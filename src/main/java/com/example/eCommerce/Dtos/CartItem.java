package com.example.eCommerce.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem implements Serializable {

    private UUID productId;
    private String productName;
    private BigDecimal unitPrice;
    private Integer quantity;

    public BigDecimal getItemSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}