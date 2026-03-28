package com.example.myecommerce.controllers;

import com.example.myecommerce.models.dto.AddProductDto;
import com.example.myecommerce.models.dto.PasswordUpdateDto;
import com.example.myecommerce.models.dto.UserUpdateDto;
import com.example.myecommerce.models.entity.Admin;
import com.example.myecommerce.models.entity.Product;
import com.example.myecommerce.models.entity.ProductType;
import com.example.myecommerce.services.ProductService;
import com.example.myecommerce.services.ProductTypeService;
import com.example.myecommerce.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final ProductService productService;
    private final ProductTypeService productTypeService;

    /**
     * Endpoint para profile
     */
    @GetMapping("/profile")
    public String adminProfile(
            Model model,
            @AuthenticationPrincipal Admin admin
    ){
        Admin adminFound=userService.findAdminByEmail(admin.getUsername());
        model.addAttribute("userData",adminFound);
        model.addAttribute("passwordUpdateDto", new PasswordUpdateDto());
        model.addAttribute("userUpdateDto", new UserUpdateDto(
                adminFound.getName(),
                adminFound.getFirstName(),
                adminFound.getEmail(),
                adminFound.getPhone(),
                adminFound.getUserAddress(),
                ""
        ));
        return "profile";
    }

    /**
     *Endpoints para catalogo, productos y productType
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/catalogue")
    public String adminCatalogue(
            @RequestParam(defaultValue = "0") int page,
            Model model
    ){
        AddProductDto addProductDto = new AddProductDto();
        Map<Long,String> productTypeList= productTypeService.getAllProductTypes();
        Page<Product> productPage = productService.getAllProductsByPage(page);
        model.addAttribute("products",productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("addProductDto", addProductDto);
        model.addAttribute("productTypeList", productTypeList);
        return "admin/catalogue";
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/add_product_type")
    public String addProductType(
            @ModelAttribute("typeDescription")String description
    ){
        productTypeService.addProductType(description);
        return "redirect:admin/catalogue";
    }

    /**
     *Endpoints para orders
     */
    @GetMapping("/orders")
    public String adminOrders(){ return "admin/orders"; }

    @GetMapping("/reports")
    public String adminReports(){ return "admin/reports"; }
}
