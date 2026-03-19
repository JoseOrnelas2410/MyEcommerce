package com.example.myecommerce.repository;

import com.example.myecommerce.models.entity.OrderStatus;
import com.example.myecommerce.repository.base.OnlyReadRepository;

public interface OrderStatusRepository extends OnlyReadRepository<OrderStatus, Long> {
}
