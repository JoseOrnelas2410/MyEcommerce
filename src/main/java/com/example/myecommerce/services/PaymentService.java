package com.example.myecommerce.services;

import com.example.myecommerce.models.dto.PaymentDto;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    @Value("${stripe.key.secret}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public PaymentIntent create(PaymentDto dto) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        Map<String,Object> params = new HashMap<>();
        params.put("amount", dto.getAmount());
        params.put("currency", dto.getCurrency());
        params.put("description", dto.getDescription());
        return PaymentIntent.create(params);
    }

}
