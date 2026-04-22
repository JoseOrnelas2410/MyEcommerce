package com.example.myecommerce.util;

import com.example.myecommerce.models.dto.DateRangeReportDto;
import com.example.myecommerce.models.dto.RankingCustomerDto;
import com.example.myecommerce.models.dto.RankingProductDto;
import com.example.myecommerce.models.entity.Order;
import com.example.myecommerce.models.entity.OrderFraction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ReportUtil {

    public static List<DateRangeReportDto> getDateRangeList(List<Order> orderList) {
        List<DateRangeReportDto> dateRangeReportDtoList = new ArrayList<>(
                orderList.stream()
                        .map(order -> {
                            return new DateRangeReportDto(
                                    order.getOrderId(),
                                    order.getCustomer().getEmail(),
                                    order.getTotal(),
                                    order.getDateTime()
                            );
                        }).toList()
        );
        dateRangeReportDtoList.sort(Comparator.comparing(DateRangeReportDto::getOrderDate).reversed());
        return dateRangeReportDtoList;
    }

    public static List<RankingProductDto> getRankingProductList(List<Order> orderList){
        List<RankingProductDto> rankingProductList = new ArrayList<>(
                orderList.stream()
                        .flatMap(order-> order.getOrderFractionsList().stream())
                        .collect(Collectors.groupingBy(OrderFraction::getProduct))
                        .entrySet()
                        .stream()
                        .map(entry-> {
                            int quantitySold = entry.getValue().stream().mapToInt(OrderFraction::getQuantity).sum();
                            return new RankingProductDto(
                                    entry.getKey().getId(),
                                    entry.getKey().getName(),
                                    quantitySold,
                                    entry.getKey().getStock(),
                                    entry.getKey().getProductType().getProductTypeDescription()
                            );
                        }).toList()
        );
        rankingProductList.sort(Comparator.comparingInt(RankingProductDto::getQuantitySold).reversed());
        return rankingProductList;
    }

    public static List<RankingCustomerDto> getRankingCustomerList(List<Order> orderList){
        List<RankingCustomerDto> rankingCustomerList = new ArrayList<>(
                orderList
                .stream()
                .collect(Collectors.groupingBy(Order::getCustomer))
                .entrySet()
                .stream()
                .map(entry ->{
                    int ordersMade = entry.getValue().size();
                    BigDecimal totalPayed= entry.getValue()
                            .stream()
                            .map(Order::getTotal)
                            .reduce(BigDecimal.ZERO,BigDecimal::add);
                    LocalDateTime lastOrderDate = entry.getValue().get(0).getDateTime();
                    return new RankingCustomerDto(
                            entry.getKey().getUsername(),
                            ordersMade,
                            totalPayed,
                            lastOrderDate
                    );
                }).toList()
        );
        rankingCustomerList.sort(Comparator.comparingInt(RankingCustomerDto::getTotalOrders).reversed());
        return rankingCustomerList;
    }





}
