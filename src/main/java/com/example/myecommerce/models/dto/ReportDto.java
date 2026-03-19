package com.example.myecommerce.models.dto;

import com.example.myecommerce.models.entity.Order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReportDto {
    LocalDateTime fromDate;
    LocalDateTime toDate;
    List<Order> orderList = new ArrayList<>();
    

}
