package com.example.myecommerce.repository;

import com.example.myecommerce.models.entity.PaymentStatus;
import com.example.myecommerce.repository.base.OnlyReadRepository;

public interface PaymentStatusRepository extends OnlyReadRepository<PaymentStatus,Long> {
}
