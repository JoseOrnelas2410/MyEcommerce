package com.example.myecommerce.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartFractionDto {
    private String imageName;

    private Long productId;

    private String productName;

    private String category;

    private BigDecimal price;

    private int quantity;

    @Override
    public String toString() {
        return "CartFractionDto{" +
                "imageName='" + imageName + '\'' +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
