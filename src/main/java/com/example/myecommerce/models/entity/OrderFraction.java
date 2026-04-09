package com.example.myecommerce.models.entity;

import com.example.myecommerce.repository.ProductRepository;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_fraction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderFraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_fraction_id", updatable = false)
    private Long orderFractionId;

    @ManyToOne
    @Setter(AccessLevel.NONE)
    @JoinColumn(name = "order_id", updatable = false, nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fraction_product")
    private Product product;

    @Column(name = "fraction_products_quantity")
    private int quantity;

    @Column(name = "fraction_unit_price")
    private BigDecimal unitPrice;

    @Column(name = "fraction_subtotal")
    private BigDecimal subtotal;

    public OrderFraction(
            Product product,
            Order order,
            int quantity,
            BigDecimal unitPrice
    ){
        this.order = order;
        this.product=product;
        this.quantity=quantity;
        this.unitPrice=unitPrice;
        subtotal=unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
