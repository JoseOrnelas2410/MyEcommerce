package com.example.myecommerce.controllers;

import com.example.myecommerce.models.dto.PasswordUpdateDto;
import com.example.myecommerce.models.dto.UserUpdateDto;
import com.example.myecommerce.models.entity.Admin;
import com.example.myecommerce.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Vistas
     */
    @GetMapping("/profile")
    public String adminProfile(
            Model model,
            @AuthenticationPrincipal Admin admin
    ){
        Admin adminFound=userService.findAdminByEmail(admin.getUsername());
        model.addAttribute("userData",adminFound);
        model.addAttribute("passwordUpdateDto", new PasswordUpdateDto());
        model.addAttribute("userUpdateDto", new UserUpdateDto());
        return "profile";
    }

    @GetMapping("/catalogue")
    public String adminCatalogue(){ return "admin/catalogue"; }

    @GetMapping("/orders")
    public String adminOrders(){ return "admin/orders"; }

    @GetMapping("/reports")
    public String adminReports(){ return "admin/reports"; }
}
