package com.example.myecommerce.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class RankingProductFraction {
    private long productId;
    private String productName;
    private int quantity;
    private int stock;
    private String productCategory;
}
