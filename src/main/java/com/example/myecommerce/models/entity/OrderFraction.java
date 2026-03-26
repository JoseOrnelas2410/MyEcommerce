package com.example.myecommerce.models.entity;

import com.example.myecommerce.repository.ProductRepository;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_fraction")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderFraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_fraction_id")
    private Long orderFractionId;

    @ManyToOne
    @Setter//Se agrega el setter ya que una ves generada la order se debe iniciar el posteo
    @JoinColumn(name = "order_id", updatable = false, nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fraction_product")
    private Product product;

    @Column(name = "fraction_products_quantity")
    private int quantity;

    @Column(name = "fraction_unit_price")
    private double unitPrice;

    @Column(name = "fraction_subtotal")
    private double subtotal;

    public OrderFraction(
            Product product,
            int quantity,
            double unitPrice
    ){
        this.product=product;
        this.quantity=quantity;
        this.unitPrice=unitPrice;
        subtotal=unitPrice*quantity;
    }
}
