package com.example.myecommerce.repository;

import com.example.myecommerce.models.dto.RankingProductFraction;
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

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    /*Busquedas para Admin*/
    Page<Order> findAllByDateTimeBetween(LocalDateTime from, LocalDateTime to, Pageable pageable);//Usado para reportes

    //El uso de Left Join permite que si no hay fractions no genere un crash
    @Query("SELECT DISTINCT o FROM Order o " + //Busqueda de orders evitando duplicados con Distinct
            "LEFT JOIN FETCH o.orderFractionsList f " + //Adjuntamos todas sus orderFraction
            "LEFT JOIN FETCH f.product p " + //Adjuntamos el Producto de cada orderFraction
            "LEFT JOIN FETCH p.productType pt " + //Adjuntamos el ProductType de cada Product
            "WHERE o.orderStatus.orderStatusDescription = 'DELIVERED' " + //Delimitamos a solo pedidos entregados
            "AND o.dateTime BETWEEN :from AND :to" //Delimitamos rango de tiempo
    )
    List<Order> findAllDeliveredWithDetailsBetween(
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

    //Necesario para balance total
    List<Order> findAllByDateTimeBetween(LocalDateTime from, LocalDateTime to);

    /*Busquedas para customer*/
    Page<Order> findAllByCustomer(Customer customer, Pageable pageable) ;//Usado para customer

}
