package com.example.myecommerce.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DateRangeReportDto {

    private Long orderId;

    private String customerName;

    private BigDecimal total;

    private LocalDateTime orderDate;

}
