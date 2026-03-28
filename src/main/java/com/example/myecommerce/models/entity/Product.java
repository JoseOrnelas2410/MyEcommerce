package com.example.myecommerce.models.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "catalogue")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "product_id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_type_id", nullable = false)
    private ProductType productType;

    @Column(name = "product_name")
    private String name;

    @Column(name = "product_price")
    private BigDecimal price;

    @Column(name = "product_stock")
    private int stock;


    @Column(name = "product_image_route", unique = true)
    private String productImageRoute;

    @Column(name = "is_active")
    private boolean isActive;

    public Product (
            ProductType productType,
            String name,
            BigDecimal price,
            int stock,
            boolean isActive
    ) {
        this.productType = productType;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.isActive = isActive;
    }

    @PrePersist
    public void setProductImageRoute() {
        this.productImageRoute = java.util.UUID.randomUUID().toString();
    }
}
