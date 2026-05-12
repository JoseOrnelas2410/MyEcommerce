package com.example.myecommerce.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private String description;//String para mostrar a ux
    private int amount;//Cantidad a pagar
    private String currency;//Tipo de moneda
}

/*const paymentIntent = await stripe.paymentIntents.create({
  amount: 2000, // 20.00 USD
  currency: 'usd',
  automatic_payment_methods: {enabled: true},
  metadata: {order_id: '12345'},
});
 */