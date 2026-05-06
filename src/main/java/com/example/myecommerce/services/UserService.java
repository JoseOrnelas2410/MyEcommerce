package com.example.myecommerce.services;

import com.example.myecommerce.models.dto.PasswordUpdateDto;
import com.example.myecommerce.models.dto.UserUpdateDto;
import com.example.myecommerce.models.entity.Admin;
import com.example.myecommerce.models.entity.Customer;
import com.example.myecommerce.models.entity.User;
import com.example.myecommerce.repository.PasswordRecoveryRepository;
import com.example.myecommerce.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
                .orElseThrow(()->new EntityNotFoundException("customer Not Found"));
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Admin findAdminByEmail(String email){
        return (Admin) userRepository.findUserByEmail(email)
                .orElseThrow(()->new EntityNotFoundException("customer Not Found"));
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
    public User updateUser(
            UserUpdateDto userUpdateValues,
            String email) throws AccessDeniedException {
        User user = findUserAndConfirmPassword(userUpdateValues.getPassword(),email);
        user.setName(userUpdateValues.getName());
        user.setFirstName(userUpdateValues.getFirstName());
        user.setEmail(userUpdateValues.getEmail());
        user.setPhone(userUpdateValues.getPhone());
        user.setUserAddress(userUpdateValues.getAddress());
        if (!refreshHttpsSession(user)) throw new IllegalArgumentException();
        return user;
    }

    /**
     * Pendiente, necesario para un refresh de las httpSession.
     * @param user
     */
    @PreAuthorize("isAuthenticated()")
    private boolean refreshHttpsSession(User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();//Obtenemos la authenticacion

        if (authentication == null) throw new IllegalArgumentException("Action not allowed");//Si no esta autenticado lanzamos exception

        UsernamePasswordAuthenticationToken refreshAuth = new UsernamePasswordAuthenticationToken( //Generamos un nuevo token de authenticacion con las credenciales y authorities
                user,
                authentication.getCredentials(),
                user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(refreshAuth);
        return true;//Al contexto almacenado en securityContext holder le enviamos la nueva session
    }


    public User userExist(String email){
        return userRepository.findUserByEmail(email)
                .orElseThrow(()-> new EntityNotFoundException("User " + email + ",  not found"));
    }


    private User findUserAndConfirmPassword(String password, String email) throws AccessDeniedException {
        User userFound = userRepository.findUserByEmail(email)
                .orElseThrow(()-> new EntityNotFoundException("User no found with email" + email));
        if (!(passwordEncoder.matches(password,userFound.getPassword()))) throw new AccessDeniedException("The actual password provided is incorrect.");
        return userFound;
    }

    public void resetPassword(User user, String newPassword){
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}
