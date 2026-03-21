package com.example.myecommerce.repository;

import com.example.myecommerce.models.entity.Customer;
import com.example.myecommerce.models.entity.User;
import com.example.myecommerce.repository.base.NoDeleteRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends NoDeleteRepository<User,Long> {
    Optional<User> getUserByEmail(String email);

    Optional<Object> getUserByUserId(Long userId);
}
