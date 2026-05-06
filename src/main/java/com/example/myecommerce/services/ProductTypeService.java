package com.example.myecommerce.services;

import com.example.myecommerce.models.entity.ProductType;
import com.example.myecommerce.repository.ProductTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductTypeService {

    private final ProductTypeRepository productTypeRepository;

    @Transactional
    @PreAuthorize("isAuthenticated()")
    public Map<Long, String> getAllProductTypes(){

        return productTypeRepository.findAll().stream()
                .collect(Collectors.toMap(
                        ProductType::getId,
                        ProductType::getProductTypeDescription
                ));
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ProductType addProductType(String description){
        return productTypeRepository.save(new ProductType(description));
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void updateCategory(Long id, String description){
        ProductType categoryToUpdate = getProductTypeById(id);
        categoryToUpdate.setProductTypeDescription(description);
    }

    public ProductType getProductTypeById(Long id){
        return productTypeRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Category not Found"));
    }
}
