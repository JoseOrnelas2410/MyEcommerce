package com.example.myecommerce.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Array;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ReportByRankingProduct {
    RankingProductFraction[] rankingProductList;
    double totalSold;
}
