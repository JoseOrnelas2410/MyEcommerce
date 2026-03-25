package com.example.myecommerce.controllers;

import com.example.myecommerce.models.entity.Admin;
import com.example.myecommerce.models.entity.Customer;
import com.example.myecommerce.models.entity.User;
import com.example.myecommerce.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/login")
    public String login(){
        return "/login";
    }

    @GetMapping("/register")//Muestra la vista
    public String showRegister(Model model){
        model.addAttribute("customer", new Customer());
        return "/register";
    }

    @PostMapping("/register")//Se dispara a traves de mi button en form
    public String register(@ModelAttribute("customer") Customer customer) {
        userService.saveUser(customer);
        return "redirect:/login?success";
    }
}
