package com.example.myecommerce.models.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ShoppingKartItem {

    private Long productId;

    private int quantity;

    private double unitPrice;

    private double subTotal;
}
