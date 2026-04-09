package com.example.myecommerce.repository;

import com.example.myecommerce.models.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Long> {

    @Override
    Optional<PaymentStatus> findById(Long aLong);
}
