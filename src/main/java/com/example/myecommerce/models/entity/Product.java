package com.example.myecommerce.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "catalogue")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "product_type_id", nullable = false)
    private ProductType productTypeId;

    @Getter
    @Setter
    @Column(name = "product_name")
    private String name;

    @Getter
    @Setter
    @Column(name = "product_price")
    private double price;

    @Getter
    @Setter
    @Column(name = "product_stock")
    private int stock;

    @Getter
    @Setter
    @Column(name = "product_image_route")
    private String productImageRoute;

    @Getter
    @Setter
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
