package com.example.myecommerce.services;

import com.example.myecommerce.config.ShoppingCart;
import com.example.myecommerce.models.dto.RankingProductFraction;
import com.example.myecommerce.models.dto.ReportByDateRangeDto;
import com.example.myecommerce.models.dto.ReportByRankingProduct;
import com.example.myecommerce.models.entity.*;
import com.example.myecommerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ShoppingCart cart;
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final UserService userService;
    private final OrderStatusService orderStatusService;
    private final PaymentStatusService paymentStatusService;

    @Transactional//Asegura que si no se cumple todo elimine los registros(Todo o nada)
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public Boolean createOrder(String userEmail, List<ShoppingKartItem> items){
        Customer customer= userService.findCustomerByEmail(userEmail);
        Order newOrder = new Order(
                customer,
                orderStatusService.getOrderStatusbyId(1L),
                paymentStatusService.getPaymentStatusById(1L),
                16,
                customer.getUserAddress());
        List<OrderFraction> fractionList = items.stream()
                .map(item-> {
                    Product product = productService.findByIdWithLock(item.getProductId());//Obtenemos con pessimisticLock

                    if (product.getStock() >= item.getQuantity()) {//Validamos el stock
                        product.setStock(product.getStock()- item.getQuantity());//Seteamos el descuento de stock a mi producto
                        productService.updateProductStock(product);//Aseguramos la persistencia de datos para el lock
                        return new OrderFraction(//Generamos la fraccion
                                product,
                                newOrder,
                                item.getQuantity(),
                                product.getPrice()
                        );
                    } else {
                        cart.getItems().stream()//De ser insuficiente modificamos carrito
                                .filter(cartItem -> cartItem.getProductId().equals(item.getProductId()))
                                .findFirst()
                                .ifPresent(cartItem -> {
                                    if (product.getStock()==0) cart.removeItem(cartItem.getProductId());//Stock Zero elimina el producto de mi ShoppingKart
                                    else cartItem.setQuantity(product.getStock());//Setea el maxStock avalible a mi carrito
                                });
                        throw new RuntimeException("We can't serve your order, insufficient stock");//Lanzamos error para interrumpir todo y evitar que se compren menos unidades sin consetimiento de user
                    }
                }).toList();
        newOrder.setOrderFractionsList(fractionList);
        orderRepository.save(newOrder);//agregamos las fracciones
        cart.getItems().clear();//Vaciamos carro para evitar que el nuevo ShoppingKart contenga residuos
        return true;
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public Page<Order> findUserOrders(int page, String email){
        Pageable pageable = PageRequest.of(page, 10, Sort.by("dateTime"));
        return orderRepository.findOrdersByCustomer(userService.findCustomerByEmail(email), pageable);
    }






















    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")//Calculo necesario para balance
    public ReportByDateRangeDto createReportByDateRange(LocalDateTime from, LocalDateTime to) {

        List<Order> ordersList = listOrderWithAllDetailsByDateRange(from, to);//Obtenemos la lista con detalles

        if(ordersList.isEmpty()) return new ReportByDateRangeDto (null, null, BigDecimal.ZERO);//De estar vacia regresamos null

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
        if(ordersList.isEmpty()) return new ReportByRankingProduct (null, BigDecimal.ZERO);
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

    private BigDecimal totalSold (List<Order> ordersList) {
        return ordersList.stream()
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
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
