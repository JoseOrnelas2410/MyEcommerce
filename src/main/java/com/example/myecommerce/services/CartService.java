package com.example.myecommerce.services;

import com.example.myecommerce.config.ShoppingCart;
import com.example.myecommerce.models.entity.Product;
import com.example.myecommerce.models.entity.ShoppingKartItem;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ShoppingCart shoppingCart;
    private final ProductService productService;

    public List<ShoppingKartItem> completeCart(){
        return shoppingCart.getItems();
    }

    public String addProduct(Long id, int quantity ){
        //Encontrar producto en el carrito
        Product product = productService.findProductById(id);
        Optional<ShoppingKartItem> item = this.shoppingCart.getItems().stream()
                .filter(cartItem -> cartItem.getProductId().equals(id))
                .findFirst();
        if (item.isPresent()){
            //Si item ya tiene max stock
            if (item.get().getQuantity() >= product.getStock()) throw new IllegalArgumentException("Product: " + product.getName() + ", Max stock reached.");
            //Si item + quantity superan a maxStock
            int newQuantity = item.get().getQuantity()+quantity; //calculo de la newQuantity si añadimos
            if (newQuantity > product.getStock()) {
                item.get().setQuantity(product.getStock());
                return "We Can Only Send You " + product.getStock() + " Items Of Product: " + product.getName() + ", We've Added This Quantity To Your Cart";
            }
            //Si item + quantity < maxStock
            item.get().setQuantity(newQuantity);
            return quantity + " Items Of Product :" + product.getName() + "Added To Your Cart";
        } else {
            this.shoppingCart.getItems().add( new ShoppingKartItem(id,quantity));
            return "Item: " + product.getName() + " Added To Your Cart";
        }
    }

    public String updateQuantity(Long id, String action){
        Product product = productService.findProductById(id);
        this.shoppingCart.getItems()
                .stream()
                .filter(cartItem -> cartItem.getProductId().equals(id))
                .findFirst()
                .ifPresent( item -> {
                    if(action.equalsIgnoreCase("increase")){
                        if (item.getQuantity()>=product.getStock()) {
                            throw new IllegalArgumentException("Max Stock Reached For Product: " + product.getName() );
                        }
                        else {
                            item.setQuantity(item.getQuantity()+1);
                        }
                    } else {
                        if (item.getQuantity()<=1) {
                            removeProduct(id);
                        }
                        else item.setQuantity(item.getQuantity()-1);
                    }
                });
        return "Items Number Of Product: " + product.getName() + action + "d On Your Cart";
    }

    public void removeProduct(Long id){
        shoppingCart.getItems().removeIf(item -> item.getProductId().equals(id));
    }

    public void setQuantity(Long id, int stock){
        shoppingCart.getItems()
                .stream()
                .filter(item-> item.getProductId().equals(id))
                .findFirst()
                .ifPresent(item-> item.setQuantity(stock));
    }

    public void clearCart() {
        this.shoppingCart.getItems().clear();
    }
}
