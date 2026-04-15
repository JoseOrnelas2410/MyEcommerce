package com.example.myecommerce.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RankingCustomerDto {

    private String name;

    private int totalOrders;

    private BigDecimal totalPayed;

    private LocalDateTime lastOrderDate;

    @Override
    public String toString() {
        return "RankingCustomerDto{" +
                "name='" + name + '\'' +
                ", totalOrders=" + totalOrders +
                ", totalPayed=" + totalPayed +
                ", lastOrderDate=" + lastOrderDate +
                '}';
    }
}
