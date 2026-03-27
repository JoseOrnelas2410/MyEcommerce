package com.example.myecommerce.controllers;

import com.example.myecommerce.models.dto.PasswordUpdateDto;
import com.example.myecommerce.models.dto.UserUpdateDto;
import com.example.myecommerce.models.entity.Customer;
import com.example.myecommerce.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/customer")
@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
@RequiredArgsConstructor
public class CustomerController {

    private final UserService userService;

    @GetMapping("/profile")
    public String customerProfile(
            Model model,
            @AuthenticationPrincipal Customer currentCustomer
    ) {
        Customer customerData = userService.findCustomerByEmail(currentCustomer.getEmail());
        model.addAttribute("userData",customerData);
        model.addAttribute("passwordUpdateDto", new PasswordUpdateDto());
        model.addAttribute("userUpdateDto", new UserUpdateDto());
        return "profile";
    }

    @GetMapping("/catalogue")
    public String cusomerCatalogue() { return "/customer/catalogue"; }

    @GetMapping("/shopping-kart")
    public String customerShoppingKart() { return "/customer/shopping-kart"; }

    @GetMapping("/orders")
    public String customerOrders() { return "/customer/orders"; }
}
