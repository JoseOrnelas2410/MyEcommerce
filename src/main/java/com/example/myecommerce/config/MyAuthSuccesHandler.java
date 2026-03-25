package com.example.myecommerce.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 *Clase generada para a partir de un AuthSuccesfull orientar al user a su controller
 */
@Component
public class MyAuthSuccesHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        var auth = authentication.getAuthorities();

        String url = auth.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")) ?
                "/admin/catalogue"
                : "/customer/catalogue";
        response.sendRedirect(url);
    }
}
