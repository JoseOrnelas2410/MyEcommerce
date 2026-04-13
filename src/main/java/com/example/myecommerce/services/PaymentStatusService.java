package com.example.myecommerce.services;

import com.example.myecommerce.models.entity.PaymentStatus;
import com.example.myecommerce.repository.PaymentStatusRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentStatusService {

    private final PaymentStatusRepository paymentStatusRepository;

    public PaymentStatus getPaymentStatusById(Long id){
        return paymentStatusRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("PAYMEN STATUS INVALID"));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Map<Long, String> findAll() {
        return paymentStatusRepository.findAll().stream()
                .collect(Collectors.toMap(
                        PaymentStatus::getPaymentStatusId,
                        PaymentStatus::getPaymentStatusDescription
                ));
    }
}
