package com.example.myecommerce.models.entity;

import com.example.myecommerce.models.dto.ReportDto;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("1")
@NoArgsConstructor
public class Admin extends User{

}