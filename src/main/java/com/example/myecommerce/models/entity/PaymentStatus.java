package com.example.myecommerce.models.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payment_status")
@Getter
@Setter
@NoArgsConstructor
public class PaymentStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_status_id")
    @Setter(AccessLevel.NONE)
    public long paymentStatusId;

    @Column(name = "payment_status_description")
    public String paymentStatusDescription;

    public PaymentStatus( String description ) {
        this.paymentStatusDescription = description;
    }
}
