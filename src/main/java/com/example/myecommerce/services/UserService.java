package com.example.myecommerce.services;

import com.example.myecommerce.models.entity.Admin;
import com.example.myecommerce.models.entity.Customer;
import com.example.myecommerce.models.entity.User;
import com.example.myecommerce.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class UserService {

    private UserRepository userRepository;

    @Transactional
    public Customer createCustomer(Customer customer){
        if (customer==null) throw new IllegalArgumentException("User cant be null");
        return userRepository.save(customer);
    }

    @Transactional
    public User userAuth(String email, String password) throws AccessDeniedException {
        var user = getUserByEmail(email);
        if (!user.getPassword().equals(password)) throw new AccessDeniedException("Credentials doesnt match");
        return user;
    }

    @Transactional User updateUsersPassword(Long id, String oldPassword, String newPassword) throws AccessDeniedException {
        User user=getUserById(id);
        if(!user.getPassword().equals(oldPassword)) throw new AccessDeniedException("Credentials doesnt match");
        user.setPassword(newPassword);
        return user;
    }

    @Transactional User recoverUsersPassword(String email, String newPassword) throws AccessDeniedException {
        User user=getUserByEmail(email);
        user.setPassword(newPassword);
        return user;
    }

    /**
     * Funciones helper para obtener internamente usuario y reusar codigo
     */

    @Transactional
    protected User getUserByEmail(String email){//Esto permitira obtener el user mediante su email desde multiles funciones de la misma clase
        return userRepository.getUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    @Transactional
    protected User getUserById(Long id){//Esto permitira obtener el user mediante su id desde multiles funciones de la misma clase
        return userRepository.findById(id) // Spring Data JPA ya ofrece findById por defecto
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }
}
