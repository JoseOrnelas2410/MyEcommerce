package com.example.myecommerce.models.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")//Indicado como Orders para evitar conflicto con palabra reservada
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Column(name = "order_id")
    private long orderId;

    @ManyToOne
    @Getter
    @JoinColumn(name = "user_id")
    Customer customer;

    @Column(name = "subtotal")
    @Getter
    private double subTotal;

    @Column(name = "iva")
    @Getter
    private double iva;

    @Column(name = "total")
    @Getter
    private double total;

    @Column(name = "order_date_time")
    @Getter
    private LocalDateTime dateTime;

    @Column(name = "order_address")
    @Getter
    private String orderAddres;

    @Column(name = "id_order_status")
    @Getter
    private int idOrderStatus;

    @Column(name = "id_payment_status")
    @Getter
    private int idPaymentStatus;

    public Order(){ }

    public Order(
            Customer customer,
            double subTotal,
            double iva,
            double total,
            String orderAddres
    ){
        this.customer = customer;
        this.subTotal = subTotal;
        this.iva = iva;
        this.total = total;
        this.dateTime = LocalDateTime.now();
        this.orderAddres = orderAddres;
    }
}
