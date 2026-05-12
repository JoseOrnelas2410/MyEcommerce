package com.example.myecommerce.config;

import com.example.myecommerce.models.entity.ShoppingKartItem;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;
import java.math.BigDecimal;
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

    @Override
    public String toString() {
        StringBuilder itemsString = new StringBuilder();
        for (ShoppingKartItem item: items){
            itemsString.append("item: ").append(item.getProductId()).append(", quantity:").append(item.getQuantity()).append("\n");
        }
        return itemsString.toString();
    }
}
