package com.example.myecommerce.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product_type")
public class ProductType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Column(name = "product_type_id")
    private long id;

    @Getter
    @Setter
    @Column(name = "product_type_description", nullable = false)
    private String description;

    public ProductType() { }

    public ProductType( String description ) {
        this.description = description;
    }
}
