package com.example.myecommerce.repository;

import com.example.myecommerce.models.entity.Customer;
import com.example.myecommerce.models.entity.Order;
import com.example.myecommerce.repository.base.NoDeleteRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends NoDeleteRepository<Order,Long> {

    List<Order> findByDateTimeBetween(LocalDateTime from, LocalDateTime to);

    List<Order> findAllByCustomer(Customer customer);

    List<Order> findOrdersByCustomerAndDateTimeBetween(Customer customer, LocalDateTime from, LocalDateTime to);

}
