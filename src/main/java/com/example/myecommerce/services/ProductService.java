package com.example.myecommerce.services;

import com.example.myecommerce.enums.StorageType;
import com.example.myecommerce.models.dto.AddProductDto;
import com.example.myecommerce.models.dto.UpdateProductDto;
import com.example.myecommerce.models.entity.Product;
import com.example.myecommerce.repository.ProductRepository;
import com.example.myecommerce.util.FileUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductTypeService productTypeService;
    private final StorageService storageService;

    public Page<Product> getAllProductsByPage(int page) {
        Pageable pageable = PageRequest.of(page, 15, Sort.by("name").ascending());
        return productRepository.findAllProductsWithDetails(pageable);
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Product saveProduct(AddProductDto addProductDto) throws IOException {
        if (addProductDto.getImage()==null) throw new IllegalArgumentException("Error, File can´t be null");
        String fileType = FileUtil.imageType(Objects.requireNonNull(addProductDto.getImage().getContentType()));
        /*Posteamos producto a su ves que convertimo el dto en un producto mediante
        constructor
         */
        Product product = productRepository.save(
                new Product(
                        productTypeService.getProductTypeById(addProductDto.getProductTypeId()),
                        addProductDto.getName(),
                        addProductDto.getPrice(),
                        addProductDto.getStock(),
                        addProductDto.isActive()
                )
        );
        String imageName= product.getProductImageRoute()+fileType;
        storageService.saveImage(addProductDto.getImage(), imageName, StorageType.PRODUCT);
        product.setExtension(fileType);
        return null;
    }

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void updateProduct(UpdateProductDto updateProductData) throws IOException {
        Product product = productRepository.getProductById(updateProductData.getId());
        if (product==null) throw new EntityNotFoundException("Product not found");
        product.setProductType(productTypeService.getProductTypeById(updateProductData.getProductTypeId()));
        product.setName(updateProductData.getName());
        product.setPrice(updateProductData.getPrice());
        product.setStock(updateProductData.getStock());
        product.setActive(updateProductData.isActive());
        if(!(updateProductData.getImage().isEmpty())) {
            String newImageType = FileUtil.imageType(Objects.requireNonNull(updateProductData.getImage().getContentType()));
            storageService.saveImage(updateProductData.getImage(), product.getProductImageRoute()+newImageType,StorageType.PRODUCT);
            product.setExtension(newImageType);
        }
    }
}
