package com.example.myecommerce.repository;

import com.example.myecommerce.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User,Long> {
    Optional<User> findUserByEmail(String email);
}
