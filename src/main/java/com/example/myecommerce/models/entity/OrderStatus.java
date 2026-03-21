package com.example.myecommerce.models.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_status")
@Getter
@Setter
@NoArgsConstructor
public class OrderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_order_status")
    @Setter(AccessLevel.NONE)
    private long orderStatusId;

    @Column(name = "order_status_description")
    private String orderStatusDescription;

    public OrderStatus(
            String description
            ){
        this.orderStatusDescription = description;
    }
}
