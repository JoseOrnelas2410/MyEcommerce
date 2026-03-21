package com.example.myecommerce.services;

import com.example.myecommerce.models.dto.OrderWithDetailsDTO;
import com.example.myecommerce.models.entity.*;
import com.example.myecommerce.repository.OrderFractionRepository;
import com.example.myecommerce.repository.OrderRepository;
import com.example.myecommerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderFractionRepository orderFractionRepository;

    private final ProductService productService;

    //Consulta Customer/Orders
    @Transactional(readOnly = true)//Optimiza recursos al ser de solo lectura
    public List<Order> findAllByCustomer(Customer customer){
        return orderRepository.findAllByCustomer(customer);
    }

    //Consulta Customer/OrderDetails
    @Transactional(readOnly = true)
    public OrderWithDetailsDTO findOrderWithDetails(Customer customer,Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if(!order.getCustomer().getUserId().equals(customer.getUserId())) {
            throw new AccessDeniedException("No tienes permiso para ver esta orden");
        }

        return new OrderWithDetailsDTO(
                customer.getName()+" "+customer.getFirstName(),
                customer.getEmail(),
                order.getTotalProducts(),
                order.getSubTotal(),
                order.getIva(),
                order.getTotal(),
                order.getDateTime(),
                order.getOrderAddress(),
                order.getOrderStatus().getOrderStatusDescription(),
                order.getPaymentStatus().getPaymentStatusDescription(),
                order.getOrderFractionsList()
        );
    }

    @Transactional
    public Order postOrder(Customer customer){
        Order newOrder = new Order(
                customer,
                new OrderStatus("Waiting"),
                new PaymentStatus("Pending"),
                16,
                customer.getUserAddress()
        );
        //Iniciamos ordenConDatosBasicos
        for (ShoppingKartItem item: customer.getShoppingKart()) {
            productService.decreaseStock(item.getProductId(),item.getQuantity());
        }
        //Hacemos el descuento del stock
        List<OrderFraction> fractions = customer.getShoppingKart().stream()
                .map(shoppingKartItem -> new OrderFraction(
                        shoppingKartItem.getProductId(),
                        shoppingKartItem.getQuantity(),
                        shoppingKartItem.getUnitPrice()
                )).toList();
        //Pasamos los ShoppingKartItem a OrderFractions
        newOrder.setOrderFractionsList(fractions);
        //Cargamos fractionsAOrder
        return orderRepository.save(newOrder);
        //
    }

}
