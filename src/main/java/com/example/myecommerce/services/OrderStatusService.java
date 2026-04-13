package com.example.myecommerce.services;

import com.example.myecommerce.models.entity.OrderStatus;
import com.example.myecommerce.repository.OrderStatusRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderStatusService {

    public final OrderStatusRepository orderStatusRepository;

    //Para generar una order
    public OrderStatus getOrderStatusById(Long id){
        return orderStatusRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("ORDER STATUS INVALID"));
    }

    /**
     * Para filtrar orders
     */
    @PreAuthorize("isAuthenticated()")
    public Map<Long, String> getAllOrderStatus(){
        return orderStatusRepository.findAll().stream()
                .collect(Collectors.toMap(
                        OrderStatus::getOrderStatusId,
                        OrderStatus::getOrderStatusDescription
                ));
    }
}
