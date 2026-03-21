package com.example.myecommerce.models.dto;

import com.example.myecommerce.models.entity.OrderFraction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderWithDetailsDTO {

    private String userCompleteName;
    private String userEmail;
    private int totalProducts;
    private double subTotal;
    private double iva;
    private double total;
    private LocalDateTime orderDateTime;
    private String address;
    private String orderStatus;
    private String paymentStatus;
    private List<OrderFraction> orderDetails = new ArrayList<>();



}
