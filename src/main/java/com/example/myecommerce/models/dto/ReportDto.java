package com.example.myecommerce.models.dto;

import com.example.myecommerce.models.entity.Order;
import com.example.myecommerce.models.entity.ReportType;
import com.example.myecommerce.models.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ReportDto {

    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private ReportType reportType;
    private List<Order> orderList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();

}
