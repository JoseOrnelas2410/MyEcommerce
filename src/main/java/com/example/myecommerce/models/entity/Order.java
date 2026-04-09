package com.example.myecommerce.models.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")//Indicado como Orders para evitar conflicto con palabra reservada
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false)
    Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    //    @JoinColumn(name = "order", updatable = false)
    private List<OrderFraction> orderFractionsList= new ArrayList<>();//Se declara final para no mover el id del heap

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_order_status")
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_payment_status")
    private PaymentStatus paymentStatus;

    @Column(name = "total_products", updatable = false)
    private int totalProducts;

    @Column(name = "subtotal", updatable = false)
    @Setter(AccessLevel.NONE)
    private BigDecimal subTotal;

    @Column(name = "iva", updatable = false)
    @Setter(AccessLevel.NONE)
    private double iva;

    @Column(name = "total", updatable = false)
    @Setter(AccessLevel.NONE)
    private BigDecimal total;

    @Column(name = "order_date_time", updatable = false)
    private LocalDateTime dateTime;

    @Column(name = "order_address")
    private String orderAddress;

    public Order(
            Customer customer,
            OrderStatus orderStatus,
            PaymentStatus paymentStatus,
            int usedIva,
            String orderAddress
    ){
        this.customer = customer;
        this.orderStatus = orderStatus;
        this.paymentStatus = paymentStatus;
        this.iva=usedIva;
        this.orderAddress = orderAddress;
    }

    @PrePersist
    public void setDateTime(){
        this.dateTime = LocalDateTime.now();
    }

    public void setOrderFractionsList(List<OrderFraction> fractions){
        this.orderFractionsList.addAll(fractions);
        this.totalProducts = orderFractionsList.stream()
                .mapToInt(OrderFraction::getQuantity)
                .sum();
        this.subTotal = orderFractionsList.stream()
                .map(OrderFraction::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.total = this.subTotal.multiply(BigDecimal.valueOf((double)iva/100).add(BigDecimal.ONE));
    }
}
