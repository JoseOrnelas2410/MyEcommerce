package com.example.myecommerce.repository;

import com.example.myecommerce.models.entity.Customer;
import com.example.myecommerce.models.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    /**
     * Customer
     */
    Page<Order> findOrdersByCustomer(Customer customer, Pageable pageable);

    @Query("SELECT o FROM Order o "+
            "LEFT JOIN FETCH o.orderFractionsList f " +
            "LEFT JOIN FETCH f.product p " +
            "LEFT JOIN FETCH p.productType " +
            "WHERE o.orderId = :id")
    Optional<Order> findOrderWithDetails(@Param("id") Long id);

    /**
     * Admin
     */
    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.orderStatus s "+
            "WHERE s.orderStatusId = :status")
    Page<Order> findByOrdersStatus(Pageable pageable,
                                   @Param("status") Long status);

    @Query("SELECT o FROM Order o " +
            "LEFT JOIN FETCH o.customer c " +
            "LEFT JOIN FETCH o.orderFractionsList f " +
            "LEFT JOIN FETCH f.product p " +
            "WHERE o.orderId = :id")
    Optional<Order> findByOrderId(@Param("id") Long id);

    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN o.orderFractionsList f " +
            "WHERE o.orderStatus.orderStatusId = 4 " +
            "AND o.dateTime BETWEEN :from AND :to " +
            "ORDER BY o.dateTime DESC "
    )
    Optional<List<Order>> findCompletedOrders(@Param("from") LocalDateTime from,
                                              @Param("to") LocalDateTime to);

    @Query("SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.orderFractionsList f " +
            "LEFT JOIN FETCH  f.product p " +
            "WHERE o.orderStatus.orderStatusId = 4L " +
            "AND o.dateTime BETWEEN :from and :to ")
    Optional<List<Order>> findOrdersAndProducts(@Param("from") LocalDateTime from,
                                                @Param("to") LocalDateTime to);
}
