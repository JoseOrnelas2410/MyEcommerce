package com.example.myecommerce.services;

import com.example.myecommerce.models.dto.RankingProductFraction;
import com.example.myecommerce.models.dto.ReportByDateRangeDto;
import com.example.myecommerce.models.dto.ReportByRankingProduct;
import com.example.myecommerce.models.entity.Order;
import com.example.myecommerce.models.entity.OrderFraction;
import com.example.myecommerce.models.entity.Product;
import com.example.myecommerce.repository.OrderRepository;
import com.example.myecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")//Calculo necesario para balance
    public ReportByDateRangeDto createReportByDateRange(LocalDateTime from, LocalDateTime to) {

        List<Order> ordersList = listOrderWithAllDetailsByDateRange(from, to);//Obtenemos la lista con detalles

        if(ordersList.isEmpty()) return new ReportByDateRangeDto (null, null, 0.0);//De estar vacia regresamos null

        List<RankingProductFraction> listRankingProduct = listRankingProducts(ordersList);//Generamos una lista de ranking product

        return new ReportByDateRangeDto(
                listRankingProduct.get(0),
                listRankingProduct.get(listRankingProduct.size()-1),
                totalSold(ordersList)//Calculamos el total con base en las ordenes obtenidas
        );
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ReportByRankingProduct createReportByRankingProduct(LocalDateTime from, LocalDateTime to){
        List<Order> ordersList = listOrderWithAllDetailsByDateRange(from, to);
        if(ordersList.isEmpty()) return new ReportByRankingProduct (null, 0.0);
        List<RankingProductFraction> listRankingProduct = listRankingProducts(ordersList);
        int limit = Math.min(listRankingProduct.size(),10);
        RankingProductFraction[] topTen = listRankingProduct.subList(0,limit)
                .toArray(new RankingProductFraction[0]);
        return new ReportByRankingProduct(
                topTen,
                totalSold(ordersList)
        );
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")//Pageable para mostrar en UI
    public Page<Order> pageOrdersByDateRange(LocalDateTime from, LocalDateTime to, int pageNum) {
        int pageSize = 15;

        Pageable pageable= PageRequest.of(pageNum, pageSize, Sort.by("dateTime"));

        return  orderRepository.findAllByDateTimeBetween(from, to, pageable);
    }

    private Double totalSold (List<Order> ordersList) {
        return ordersList.stream()
                .mapToDouble(Order::getTotal)
                .sum();
    }

    private List<RankingProductFraction> listRankingProducts (List<Order> orders){//Obtencion de la lista y mapeo a List<RankingProduct>
        return orders.stream()//recorremos orders
                .flatMap(o -> o.getOrderFractionsList().stream())//recorremos las orderFraction de cada orden
                .collect(Collectors.groupingBy(//Recolectamos agrupando por Producto y cantidad
                        OrderFraction::getProduct,
                        Collectors.summingInt(OrderFraction::getQuantity)
                ))//Generamos el map correspondiente
                .entrySet().stream()//Damos entrada por valores de mi Map
                .sorted(Map.Entry.<Product, Integer>comparingByValue().reversed())//organizamos conforme a cantidad vendida
                .map(entry -> new RankingProductFraction(//Mapeamos nuevamente para generar RankingProductFractions
                        entry.getKey().getId(),//Id
                        entry.getKey().getName(),//Name
                        entry.getValue(),//Quantity
                        entry.getKey().getStock(),//Stock
                        entry.getKey().getProductType().getProductTypeDescription()//ProductCategory
                ))
                .collect(Collectors.toList());//Lo convertimos a una lista
    }


    private List<Order> listOrderWithAllDetailsByDateRange(LocalDateTime from, LocalDateTime to) {
        return orderRepository.findAllDeliveredWithDetailsBetween(from, to);
    }
}
