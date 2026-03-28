package com.example.myecommerce.services;

import com.example.myecommerce.models.dto.PasswordUpdateDto;
import com.example.myecommerce.models.dto.UserUpdateDto;
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
            String userEmail) throws AccessDeniedException {
        //Verificamos que el newPassword no este vacio o null
        if(passwordUpdateValues.getNewPassword().isEmpty() || passwordUpdateValues.getNewPassword().isBlank()) throw new IllegalArgumentException("New password can´t be empty or blank");
        //Confirmamos coincidencia de password
        User actualUser = findUserAndConfirmPassword(
                passwordUpdateValues.getOldPassword(),
                userEmail);
        actualUser.setPassword(passwordEncoder.encode(passwordUpdateValues.getNewPassword()));
    }

    @PreAuthorize("isAuthenticated()")
    @Transactional
    public boolean updateUser(
            UserUpdateDto userUpdateValues,
            String email) throws AccessDeniedException {
        User user = findUserAndConfirmPassword(userUpdateValues.getPassword(),email);
        user.setName(userUpdateValues.getName());
        user.setFirstName(userUpdateValues.getFirstName());
        /*
        Verificamos si existe cambio de email, de haberlo puede generar conflicto en
        con la sesion abierta y arrojar error.
         */
        if (!user.getEmail().equals(userUpdateValues.getEmail())) {
            user.setEmail(userUpdateValues.getEmail());
            return true;
        }
        user.setPhone(userUpdateValues.getPhone());
        user.setUserAddress(userUpdateValues.getAddress());
        return false;
    }

    private User findUserAndConfirmPassword(String password, String email) throws AccessDeniedException {
        User userFound = userRepository.findUserByEmail(email)
                .orElseThrow(()-> new EntityNotFoundException("User no found with email" + email));
        if (!(passwordEncoder.matches(password,userFound.getPassword()))) throw new AccessDeniedException("The actual password provided is incorrect.");
        return userFound;
    }
}
