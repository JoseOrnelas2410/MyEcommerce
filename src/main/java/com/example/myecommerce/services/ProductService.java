package com.example.myecommerce.services;

import com.example.myecommerce.models.entity.Product;
import com.example.myecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Page<Product> getAllProductsByPage(int page) {
        Pageable pageable = PageRequest.of(page, 15, Sort.by("name").ascending());
        return productRepository.findAllProductsWithDetails(pageable);
    }
}
