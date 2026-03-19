package com.example.myecommerce.repository;

import com.example.myecommerce.models.entity.ProductType;
import com.example.myecommerce.repository.base.OnlyReadRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTypeRepository extends OnlyReadRepository<ProductType, Long> { }
