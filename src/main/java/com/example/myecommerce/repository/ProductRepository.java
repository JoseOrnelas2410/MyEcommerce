package com.example.myecommerce.repository;

import com.example.myecommerce.models.entity.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Override
    Optional<Product> findById(Long aLong);

    //Query's para clientes
    @Query("SELECT DISTINCT p FROM Product p "+
    "LEFT JOIN FETCH p.productType "+
    "WHERE p.isActive = true "+
    "AND p.stock > 0 ")
    Page<Product> findAllActiveProducts(Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p " +
            "LEFT JOIN FETCH p.productType c " +
            "WHERE p.isActive = true " +
            "AND p.stock > 0" +
            "AND c.id = :category")
    Page<Product> findActiveProductsByCategory(Pageable pageable,
                                               @Param("category") Long category);

    //Query para lista de admin
    @Query("SELECT DISTINCT p FROM Product p "+
    "LEFT JOIN FETCH p.productType")
    Page<Product> findAllProductsWithDetails(Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id ")
    Optional<Product> findByIdWithLock(@Param("id")Long id);
}
