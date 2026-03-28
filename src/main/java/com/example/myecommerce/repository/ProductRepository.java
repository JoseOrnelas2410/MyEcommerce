package com.example.myecommerce.repository;

import com.example.myecommerce.models.entity.Product;
import jakarta.persistence.Entity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Product getProductById(Long id);

    //Query para catalogo de clientes
    @Query("SELECT DISTINCT p FROM Product p "+
    "LEFT JOIN FETCH p.productType "+
    "WHERE p.isActive = true "+
    "AND p.stock > 0 ")
    Page<Product> findAllActiveProducts(Pageable pageable);

    //Query para lista de admin
    @Query("SELECT DISTINCT p FROM Product p "+
    "LEFT JOIN FETCH p.productType")
    Page<Product> findAllProductsWithDetails(Pageable pageable);


}
