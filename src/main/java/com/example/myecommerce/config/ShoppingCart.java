package com.example.myecommerce.config;

import com.example.myecommerce.models.entity.ShoppingKartItem;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@SessionScope
@Getter
@Setter
public class ShoppingCart implements Serializable {
    private List<ShoppingKartItem> items = new ArrayList<>();

    public void addItem(Long id, int quantity) {
        Optional<ShoppingKartItem> optionalItem = items.stream()
                .filter(cartItem-> cartItem.getProductId().equals(id))
                .findFirst();
        if (optionalItem.isPresent()) {
            ShoppingKartItem item = optionalItem.get();
            item.setQuantity(item.getQuantity()+quantity);
        } else {
            items.add(new ShoppingKartItem(id,quantity));
        }
    }



    public void updateQuantity(Long id, String action, int maxStock){
        items.stream().filter(item->item.getProductId().equals(id))
                .findFirst()
                .ifPresent(item-> {
                    if (action.equalsIgnoreCase("decrease")) {
                        if (item.getQuantity()<=1) removeItem(item.getProductId());
                        else item.setQuantity(item.getQuantity()-1);
                    }
                    if (action.equalsIgnoreCase("increase")) {
                        if (item.getQuantity()>=maxStock) throw new IllegalArgumentException("Max Stock reached");
                        else item.setQuantity(item.getQuantity()+1);
                    }
                });
    }

    public void decreaseQuantity(Long id) {
        items.stream().filter(item->item.getProductId().equals(id))
                .findFirst()
                .ifPresent( item -> {
                    if (item.getQuantity()<=1) {
                        removeItem(id);
                    } else {
                        item.setQuantity(item.getQuantity()-1);
                    }
                });
    }

    public void removeItem(Long id){
        items.removeIf(item->item.getProductId().equals(id));//Recorremos y donde el id sea identico eliminamos producto
    }

    @Override
    public String toString() {
        StringBuilder itemsString = new StringBuilder();
        for (ShoppingKartItem item: items){
            itemsString.append("item: ").append(item.getProductId()).append(", quantity:").append(item.getQuantity()).append("\n");
        }
        return itemsString.toString();
    }
}
