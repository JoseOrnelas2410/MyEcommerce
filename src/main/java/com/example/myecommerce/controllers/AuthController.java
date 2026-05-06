package com.example.myecommerce.controllers;

import com.example.myecommerce.models.dto.PasswordUpdateDto;
import com.example.myecommerce.models.dto.UserUpdateDto;
import com.example.myecommerce.models.entity.Admin;
import com.example.myecommerce.models.entity.Customer;
import com.example.myecommerce.models.entity.User;
import com.example.myecommerce.services.EmailService;
import com.example.myecommerce.services.PasswordRecoveryService;
import com.example.myecommerce.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.AccessDeniedException;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final EmailService emailService;
    private final PasswordRecoveryService passwordRecoveryService;

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
            //Buscar @Valid para implementar verificacion de campos
            @ModelAttribute("passwordUpdateDto")PasswordUpdateDto passwordUpdateDto,
            @AuthenticationPrincipal User user,
            RedirectAttributes redirectAttributes) throws AccessDeniedException {
        userService.updatePassword(passwordUpdateDto, user.getUsername());
        redirectAttributes.addFlashAttribute("success","Password Updated");
        return (user instanceof Admin) ? "redirect:/admin/profile" : "redirect:/customer/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("update_profile")
    public String updateProfile(
            //Buscar @Valid para implementar verificacion de campos
            @ModelAttribute("userUpdateDto")UserUpdateDto userUpdateDto,
            @AuthenticationPrincipal User user,
            RedirectAttributes redirectAttributes) throws AccessDeniedException {
        User userUpdated = userService.updateUser(userUpdateDto, user.getUsername());
        System.out.println("Profile updated agregando redirect attributes");
        redirectAttributes.addFlashAttribute("success","Profile Updated");
        if (userUpdated instanceof Admin) return "redirect:/admin/profile";//Si se cambia el email es necesario desloguear a user para no mostrar error y solicitar nuevo login.
        else return "redirect:/customer/profile";
    }

    @GetMapping("/password_recovery")
    public String passwordRecovery(
    ){
        return "/password-recovery";
    }

    @PostMapping("/start_password_recovery")
    public String setPasswordRecoveryMail(
            @RequestParam (name = "mail", required = true) String email
    ){
        try {
            emailService.sendHtmlTemplateMail(email);//Si se logra enviar te regresa a login
            return "redirect:/login";
        } catch (Exception e) {
            System.out.println("Error Exception:  " + e.getMessage());
            return "redirect:/password_recovery";//Si no se envia te manda a la misma pagina
        }
    }

    @GetMapping("/reset_password")
    public String resetPassword(
            @RequestParam(name = "token", required = true) String token,
            Model model
    ){
        model.addAttribute("token", token);
        return "/reset-password";
    }

    @PostMapping("/recover_password")
    public String recoverPassword(
            @RequestParam(name = "token") String token,
            @RequestParam(name = "password") String password
    ){
        System.out.println("Data from ui {token : " + token + ", password " + password + "}");
        passwordRecoveryService.recoverPassword(token, password);
        return "redirect:/login";
    }


}
