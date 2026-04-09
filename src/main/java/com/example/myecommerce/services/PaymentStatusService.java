package com.example.myecommerce.services;

import com.example.myecommerce.models.entity.PaymentStatus;
import com.example.myecommerce.repository.PaymentStatusRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentStatusService {

    private final PaymentStatusRepository paymentStatusRepository;

    public PaymentStatus getPaymentStatusById(Long id){
        return paymentStatusRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("PAYMEN STATUS INVALID"));
    }
}
