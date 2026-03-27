package com.example.myecommerce.services;

import com.example.myecommerce.models.dto.PasswordUpdateDto;
import com.example.myecommerce.models.entity.Admin;
import com.example.myecommerce.models.entity.Customer;
import com.example.myecommerce.models.entity.User;
import com.example.myecommerce.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public Customer findCustomerByEmail(String email){
        return (Customer) userRepository.findUserByEmail(email)
                .orElseThrow(()->new EntityNotFoundException("customerNotFound"));
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Admin findAdminByEmail(String email){
        return (Admin) userRepository.findUserByEmail(email)
                .orElseThrow(()->new EntityNotFoundException("customerNotFound"));
    }


    public User saveCustomer(Customer customer){
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return userRepository.save(customer);
    }

    @PreAuthorize("isAuthenticated()")
    @Transactional
    public void updatePassword(
            PasswordUpdateDto passwordUpdateValues,
            User user) throws AccessDeniedException {
        //Verificamos que el newPassword no este vacio o null
        if(passwordUpdateValues.getNewPassword().isEmpty() || passwordUpdateValues.getNewPassword().isBlank()) throw new IllegalArgumentException("New password can´t be empty or blank");
        //Buscamos user
        User userFound = userRepository.findUserByEmail(user.getEmail())
                .orElseThrow(()-> new EntityNotFoundException("Current user not found"));
        //Confirmamos coincidencia de password
        boolean matches = passwordEncoder.matches(passwordUpdateValues.getOldPassword(),userFound.getPassword());
        if (matches) {
            userFound.setPassword(passwordEncoder.encode(passwordUpdateValues.getNewPassword()));//seteamos new password encriptada
        } else {
            throw new IllegalArgumentException("The actual password provided is incorrect.");
        }
    }
}
