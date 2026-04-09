package com.example.myecommerce.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ReportByDateRangeDto {
    RankingProductFraction topProduct = new RankingProductFraction();
    RankingProductFraction bottomProduct = new RankingProductFraction();
    BigDecimal totalSold;
}
