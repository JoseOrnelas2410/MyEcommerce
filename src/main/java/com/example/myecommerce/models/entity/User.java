package com.example.myecommerce.models.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)//Genera una sola tabla para User ya sean Customer o Admin
@DiscriminatorColumn(name = "user_type_id", discriminatorType = DiscriminatorType.INTEGER)//Aqui radica el cambio entre Customer y Admin
@Getter
@Setter

public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "user_id")
    private long userId;

    @Setter(AccessLevel.NONE)
    @Column(name = "password")
    private String password;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_firstname")
    private String firstName;

    @Column(name = "user_email")
    private String email;

    @Column(name = "user_phone")
    private long phone;

    @Column(name = "user_address")
    private String userAddress;

    protected User() {
    }

    protected User(
            String password,
            String name,
            String firstName,
            String email,
            long phone,
            String address
    ){
        this.password = password;
        this.name = name;
        this.firstName = firstName;
        this.email = email;
        this.phone = phone;
        this.userAddress = address;
    }

    public void updatePassword(String newPassword){
        this.password=newPassword;
    }
}
