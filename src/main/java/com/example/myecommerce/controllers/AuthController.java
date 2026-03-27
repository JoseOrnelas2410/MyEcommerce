package com.example.myecommerce.controllers;

import com.example.myecommerce.models.dto.PasswordUpdateDto;
import com.example.myecommerce.models.entity.Admin;
import com.example.myecommerce.models.entity.Customer;
import com.example.myecommerce.models.entity.User;
import com.example.myecommerce.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.AccessDeniedException;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping("/login")//recibe esta liga
    public String login(){
        return "login";
    } //retorna este archivo html

    @GetMapping("/register")//Muestra la vista
    public String showRegister(Model model){
        model.addAttribute("customer", new Customer());
        return "/register";
    }

    @PostMapping("/register")//Se dispara a traves de mi button en form
    public String register(@ModelAttribute("customer") Customer customer) {
        userService.saveCustomer(customer);
        return "redirect:/login?success";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("update_password")
    public String updatePassword(
            @ModelAttribute("passwordUpdateDto")PasswordUpdateDto passwordUpdateDto,
            @AuthenticationPrincipal User user,
            RedirectAttributes redirectAttributes){
        System.out.println("Aviso controllerAuth metodo updatePassword");
        try {
            userService.updatePassword(passwordUpdateDto, user);
            redirectAttributes.addFlashAttribute("success","password updated succesfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",e.getCause());
        }
        System.out.println("Aviso controllerAuth metodo updatePassword: userService llamado, retornando vista");
        return (user instanceof Admin) ? "redirect:/admin/profile":"redirect:/customer/profile";//Aqui que me recomiendas devolver para llamar al controller indicado?
    }
}
