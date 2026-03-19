package com.example.myecommerce.models.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")//Indicado como Orders para evitar conflicto con palabra reservada
@Getter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private long orderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    Customer customer;

    @Column(name = "subtotal")
    private double subTotal;

    @Column(name = "iva")
    private double iva;

    @Column(name = "total")
    private double total;

    @Column(name = "order_date_time")
    private LocalDateTime dateTime;

    @Column(name = "order_address")
    private String orderAddres;

    @Column(name = "id_order_status")
    @Setter
    private int idOrderStatus;

    @Column(name = "id_payment_status")
    @Setter
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
        this.idOrderStatus = 1;
        this.idPaymentStatus = 1;
    }
}
