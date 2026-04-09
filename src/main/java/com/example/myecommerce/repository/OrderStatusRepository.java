package com.example.myecommerce.repository;

import com.example.myecommerce.models.entity.OrderStatus;
import com.example.myecommerce.services.OrderService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
    @Override
    Optional<OrderStatus> findById(Long id);
}
