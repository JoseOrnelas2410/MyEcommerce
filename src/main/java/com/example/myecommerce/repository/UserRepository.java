package com.example.myecommerce.repository;

import com.example.myecommerce.models.entity.User;
import com.example.myecommerce.repository.base.NoDeleteRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends NoDeleteRepository<User,Long> { }
