package com.example.myecommerce.repository;

import com.example.myecommerce.models.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product getProductById(Long id);

}
