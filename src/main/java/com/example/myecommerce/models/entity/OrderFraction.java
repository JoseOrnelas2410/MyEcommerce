package com.example.myecommerce.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_fraction")
@Getter
public class OrderFraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_fraction_id")
    private Long orderFractionId;

    @ManyToOne
    @Setter//Se agrega el setter ya que una ves generada la order se debe iniciar el posteo
    @JoinColumn(name = "order_id", updatable = false, nullable = false)
    private Order order;

    @Column(name = "fraction_product_id")
    private Long productId;

    @Column(name = "fraction_products_quantity")
    private int quantity;

    @Column(name = "fraction_unit_price")
    private double unitPrice;

    @Column(name = "fraction_subtotal")
    private double subtotal;

    public OrderFraction(){
    }

    public OrderFraction(
            Long productId,
            int quantity,
            double unitPrice
    ){
        this.productId=productId;
        this.quantity=quantity;
        this.unitPrice=unitPrice;
        subtotal=unitPrice*quantity;
    }
}
