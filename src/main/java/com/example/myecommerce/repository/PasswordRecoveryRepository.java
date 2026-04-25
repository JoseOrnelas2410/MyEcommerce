package com.example.myecommerce.repository;

import com.example.myecommerce.models.entity.PasswordRecovery;
import com.example.myecommerce.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordRecoveryRepository extends JpaRepository<PasswordRecovery, Long> {

    Optional<PasswordRecovery> getPasswordRecoveryByToken(String token);

    PasswordRecovery getPasswordRecoveryByUser(User user);
}
