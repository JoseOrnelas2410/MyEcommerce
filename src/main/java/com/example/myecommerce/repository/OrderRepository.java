package com.example.myecommerce.repository;

import com.example.myecommerce.models.entity.Order;
import com.example.myecommerce.repository.base.NoDeleteRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends NoDeleteRepository<Order,Long> {

    //Añadir @Query para obtener las ordenes en un periodo x de tiempo

}
