package com.example.myecommerce.services;

import com.example.myecommerce.models.entity.OrderStatus;
import com.example.myecommerce.repository.OrderStatusRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderStatusService {

    public final OrderStatusRepository orderStatusRepository;

    public OrderStatus getOrderStatusbyId(Long id){
        return orderStatusRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("ORDER STATUS INVALID"));
    }
}
