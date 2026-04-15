package com.example.myecommerce.models.dto;

import com.example.myecommerce.models.entity.ProductType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RankingProductDto {

    private Long productId;

    private String name;

    private int quantitySold;

    private int actualStock;

    private ProductType category;

    @Override
    public String toString() {
        return "RankingProductDto{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", quantitySold=" + quantitySold +
                ", actualStock=" + actualStock +
                ", category=" + category +
                '}';
    }
}
