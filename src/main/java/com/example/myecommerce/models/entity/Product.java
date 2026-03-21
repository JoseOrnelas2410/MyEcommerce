package com.example.myecommerce.models.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "catalogue")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "product_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_type_id", nullable = false)
    private ProductType productTypeId;

    @Column(name = "product_name")
    private String name;

    @Column(name = "product_price")
    private double price;

    @Column(name = "product_stock")
    private int stock;

    @Column(name = "product_image_route")
    private String productImageRoute;

    @Column(name = "is_active")
    private boolean isActive;

    public Product () { }

    public Product (
            ProductType productType,
            String name,
            double price,
            int stock,
            String productImageRoute,
            boolean isActive
    ) {
        this.productTypeId = productType;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.productImageRoute = productImageRoute;
        this.isActive = isActive;

    }
}
