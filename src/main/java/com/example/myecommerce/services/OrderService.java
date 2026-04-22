package com.example.myecommerce.services;

import com.example.myecommerce.config.ShoppingCart;
import com.example.myecommerce.models.dto.DateRangeReportDto;
import com.example.myecommerce.models.dto.RankingCustomerDto;
import com.example.myecommerce.models.dto.RankingProductDto;
import com.example.myecommerce.models.entity.*;
import com.example.myecommerce.repository.OrderRepository;
import com.example.myecommerce.util.ReportUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
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

    /**
     * Customer
     */

    @Transactional//Asegura que si no se cumple todo elimine los registros(Todo o nada)
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public Boolean createOrder(String userEmail, List<ShoppingKartItem> items){
        Customer customer= userService.findCustomerByEmail(userEmail);
        Order newOrder = new Order(
                customer,
                orderStatusService.getOrderStatusById(1L),
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

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public Page<Order> findUserOrders(int page, String email){
        Pageable pageable = PageRequest.of(page, 10, Sort.by("dateTime"));
        return orderRepository.findOrdersByCustomer(userService.findCustomerByEmail(email), pageable);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    public Order getOrderByUser(Long id, String email) throws IllegalAccessException {
         Order orderFound = orderRepository.findOrderWithDetails(id).orElseThrow(() -> new EntityNotFoundException("Order Not found"));
         if (orderFound.getCustomer().getUsername().equalsIgnoreCase(email)) {
             return orderFound;
         } else { throw new IllegalAccessException("You dont have access to this order.");
         }
    }

    /**
     * Admin
     */

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Page<Order> getOrders(int pageNumber, Long orderStatus) {
        Pageable pageable = PageRequest.of(pageNumber,15,Sort.by("dateTime").descending());
        if (orderStatus > 0 && orderStatus <= 4) {
            return orderRepository.findByOrdersStatus(pageable, orderStatus);
        } else { return orderRepository.findAll(pageable); }
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Order getOrderById(Long id){
        return orderRepository.findById(id)
                        .orElseThrow(()->new EntityNotFoundException("Order not found"));
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void updateOrder(Long id, Long paymentStatus, Long orderStatus) {
        Order orderToUpdate = orderRepository.findByOrderId(id)
                .orElseThrow(()->new EntityNotFoundException("Order Not Found"));
        if (orderToUpdate.getPaymentStatus().getPaymentStatusId() == 3L) {//Si se detecta orden pagada procedemos a actualizar el estado de la orden
            if (orderToUpdate.getOrderStatus().getOrderStatusId()>orderStatus) throw new IllegalArgumentException("ORDER STATUS CAN'T BE DOWNGRADED");//Evita downgrade en status
            orderToUpdate.setOrderStatus(orderStatusService.getOrderStatusById(orderStatus));
        } else {
            orderToUpdate.setPaymentStatus(paymentStatusService.getPaymentStatusById(paymentStatus));//Genera cambio de orderStatus
        }
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Map<Integer, Object> createReport(int reportType,
                                             Date from,
                                             Date to
    ) {
        if (from == null || to == null) throw new IllegalArgumentException("No dates Selected");
        LocalDateTime fromLDT= from.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime toLDT= to.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        List<Order> orders = orderRepository.findCompletedOrders(fromLDT, toLDT);
        if (orders.isEmpty()) throw new EntityNotFoundException("No orders completed in this date Range");
        AtomicInteger rank = new AtomicInteger(1);
        Map<Integer, Object> map = new LinkedHashMap<>();
        switch (reportType) {
            case 1:
                List<DateRangeReportDto> dateRangeReportList = ReportUtil.getDateRangeList(orders);
                dateRangeReportList.forEach(dto -> {
                    map.put(
                            rank.getAndIncrement(),
                            dto
                    );
                });
                return map;
            case 2:
                List<RankingProductDto> rankingProductList = ReportUtil.getRankingProductList(orders);
                //Llenamos map
                rankingProductList.forEach(rankedProduct->{
                    map.put(
                            rank.getAndIncrement(),
                            rankedProduct
                    );
                });
                return map;
            case 3:
                //Lista
                List<RankingCustomerDto> rankedCustomers = ReportUtil.getRankingCustomerList(orders);
                rankedCustomers.forEach(rankingCustomer->{
                    map.put(
                            rank.getAndIncrement(),
                            rankingCustomer
                    );
                });
                return map;
            default:
                throw new IllegalArgumentException("Invalid Report Type");
        }
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<Order> getOrdersByDateRange(Date from, Date to){
        LocalDateTime from_date = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime to_date = to.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        List<Order> ordersFound = orderRepository.findCompletedOrders(from_date, to_date);
        if (ordersFound.isEmpty()) throw new EntityNotFoundException("No ordersFound in this dateRange");
        return ordersFound;
    }
}
