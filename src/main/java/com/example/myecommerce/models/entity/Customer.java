package com.example.myecommerce.models.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("2")
public class Customer extends  User{

    //De ser volatil y no guardarse usar @Tansient de quere una tabla usar @OneToMany y cambiar a entity mi shoppingKartItem
    @Transient // Evita que se genere una tabla para mi ShoppingKart
    private List<ShoppingKartItem> shoppingKart = new ArrayList<>();

    public Customer (){
    }

    public Customer (
            String password,
            String name,
            String firstName,
            String email,
            long phone,
            String address
    ){
        super(password, name, firstName, email, phone, address);
    }

    /**
     *Funciones para añadir a mi shoppingKart
     */

    public void addToShoppingKart(ShoppingKartItem itemToAdd){
        boolean itemFound=false;
        for (ShoppingKartItem item: shoppingKart){
            if (item.getProductId().equals(itemToAdd.getProductId())){
                itemFound = true;
                item.setQuantity(item.getQuantity()+itemToAdd.getQuantity());
                break;
            }
        }
        if (!itemFound) { shoppingKart.add(itemToAdd); }
    }

    public void removeItemFromShoppingKart(String idToRemove){
        for (ShoppingKartItem item: shoppingKart){
            if (item.getProductId().equals(idToRemove)){
                shoppingKart.remove(item);
                break;
            }
        } // shoppingKart.removeIf(item -> item.getProductId().equals(idToRemove));
    }

    public void updateItemQuantityFromShoppingKart(String productId,int quantity){
        for (ShoppingKartItem item: shoppingKart){
            if (item.getProductId().equals(productId)){
                item.setQuantity(quantity);
                break;
            }
        }
    }

}
