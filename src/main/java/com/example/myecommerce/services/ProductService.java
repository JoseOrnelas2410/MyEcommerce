package com.example.myecommerce.services;

import com.example.myecommerce.models.entity.Admin;
import com.example.myecommerce.models.entity.Product;
import com.example.myecommerce.models.entity.User;
import com.example.myecommerce.repository.ProductRepository;
import com.example.myecommerce.repository.ProductTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final UserService userService;

    private final ProductRepository productRepository;

    private final ProductTypeRepository productTypeRepository;

    /**
     * Funciones para admin
     */
    @Transactional
    public Product createProduct(Product product, Long userID) throws IllegalAccessException {
        User user = userService.getUserById(userID);
        if (!(user instanceof Admin)) throw new IllegalAccessException("You have no access to this function");
        if (product == null) throw new IllegalArgumentException("Product cant be null");
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Product findProductByIDWithLock(Long id, Long userId) {
        /**Editar funcion en orden de que si un customer lo usa no se muestren los active false pero si un
         * admin lo usa mostrar aunque no este activo
         */
        User user = userService.getUserById(id)
        return productRepository.findById(id)
                .filter(Product::isActive)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }

    @Transactional
    public void setProductActive(Long id, boolean isActive){
        Product productToDisable = findProductByIDWithLock(id);
        productToDisable.setActive(false);
    }

    @Transactional
    public void updateStock(Long id,int newStock){
        Product productToUpdate = findProductByIDWithLock(id);

        if (newStock<0) throw new IllegalArgumentException("Quantity must be higher than 0");
        if (newStock==0) disableProduct(id);
        productToUpdate.setStock(newStock);
    }

    @Transactional
    public void decreaseStock(Long id, int quantity){
        if (quantity <= 0)throw new IllegalArgumentException("Quantity must be higher than 0");

        Product productToUpdate = productRepository.findByIdWithLock(id)
                .orElseThrow(() -> new EntityNotFoundException("ProductNotFound"));

        if ((productToUpdate.getStock()-quantity)<0) throw new IllegalArgumentException("To many products, out of stock");
        productToUpdate.setStock(productToUpdate.getStock()-quantity);
    }

}
