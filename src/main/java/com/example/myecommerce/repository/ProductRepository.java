package com.example.myecommerce.repository;

import com.example.myecommerce.models.entity.Product;
import com.example.myecommerce.repository.base.NoDeleteRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends NoDeleteRepository<Product, Long> { }
