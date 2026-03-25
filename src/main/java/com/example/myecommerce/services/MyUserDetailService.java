package com.example.myecommerce.services;

import com.example.myecommerce.models.entity.User;
import com.example.myecommerce.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service generado para funcionar en conjunto con SpringSecurity
 * necesario para delegar la administracion de roles
 */

@Service
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        email=email.trim();
        System.out.println("Email"+ email);
        return userRepository.findUserByEmail(email)
                .orElseThrow(()->{
                    System.out.println("Usuario no encontrado en BD");
                    return new UsernameNotFoundException("User not found with email");
                });
    }

}
