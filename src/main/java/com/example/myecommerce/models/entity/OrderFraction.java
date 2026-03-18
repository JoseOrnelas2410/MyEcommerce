package com.example.myecommerce.models.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "order_fraction")
public class OrderFraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_fraccion_id")
    @Getter
    private long orderFractionId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "product_id")
    @Getter
    private String productId;

    @Column(name = "quantity")
    @Getter
    private int quantity;

    @Column(name = "unit_price")
    @Getter
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
