package com.example.myecommerce.models.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "order_fraction")
@Getter
public class OrderFraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_fraccion_id")
    private long orderFractionId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "product_id")
    private String productId;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "unit_price")
    private double unitPrice;

    public OrderFraction(){
    }

    public OrderFraction(
            Order order,
            String productId,
            int quantity,
            double unitPrice
    ){
        this.order = order;
        this.productId=productId;
        this.quantity=quantity;
        this.unitPrice=unitPrice;
    }
}
