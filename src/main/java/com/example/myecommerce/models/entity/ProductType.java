package com.example.myecommerce.models.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product_type")
@Getter
@Setter
public class ProductType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_type_id")
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(name = "product_type_description", nullable = false)
    private String productTypeDescription;

    public ProductType() { }

    public ProductType( String description ) {
        this.productTypeDescription = description;
    }
}
