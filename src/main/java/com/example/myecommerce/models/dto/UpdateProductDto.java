package com.example.myecommerce.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductDto {
    private Long id;
    private MultipartFile image;
    private String name;
    private BigDecimal price;
    private int stock;
    private Long productTypeId;
    private boolean active;

    @Override
    public String toString() {
        return "UpdateProductDto{" +
                "id=" + id +
                ", image=" + image.getName() +
                ", name=" + name +
                ", price=" + price +
                ", stock=" + stock +
                ", productTypeId=" + productTypeId +
                ", active=" + active +
                '}';
    }
}
