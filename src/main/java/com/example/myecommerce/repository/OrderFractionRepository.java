package com.example.myecommerce.repository;

import com.example.myecommerce.models.entity.OrderFraction;
import com.example.myecommerce.repository.base.NoDeleteRepository;
import org.springframework.data.repository.Repository;
import org.yaml.snakeyaml.events.Event;

import java.util.List;

public interface OrderFractionRepository extends NoDeleteRepository<OrderFraction, Long> { }
