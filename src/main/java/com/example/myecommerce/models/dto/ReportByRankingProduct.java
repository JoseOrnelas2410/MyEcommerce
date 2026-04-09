package com.example.myecommerce.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Array;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ReportByRankingProduct {
    RankingProductFraction[] rankingProductList;
    BigDecimal totalSold;
}
