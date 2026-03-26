package com.example.myecommerce.models.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("1")
@NoArgsConstructor
public class Admin extends User{

}