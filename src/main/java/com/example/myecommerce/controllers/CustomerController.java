package com.example.myecommerce.controllers;

import com.example.myecommerce.config.ShoppingCart;
import com.example.myecommerce.models.dto.CartFractionDto;
import com.example.myecommerce.models.dto.PasswordUpdateDto;
import com.example.myecommerce.models.dto.UserUpdateDto;
import com.example.myecommerce.models.entity.*;
import com.example.myecommerce.services.OrderService;
import com.example.myecommerce.services.ProductService;
import com.example.myecommerce.services.ProductTypeService;
import com.example.myecommerce.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/customer")
@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
@RequiredArgsConstructor
public class CustomerController {

    private final ShoppingCart shoppingCart;
    private final UserService userService;
    private final ProductService productService;
    private final ProductTypeService productTypeService;
    private final OrderService orderService;

    /**
     * Profile
     */

    @GetMapping("/profile")
    public String customerProfile(
            Model model,
            @AuthenticationPrincipal Customer currentCustomer
    ) {
        Customer customerFound = userService.findCustomerByEmail(currentCustomer.getEmail());
        model.addAttribute("userData",customerFound);
        model.addAttribute("passwordUpdateDto", new PasswordUpdateDto());
        model.addAttribute("userUpdateDto", new UserUpdateDto(
                customerFound.getName(),
                customerFound.getFirstName(),
                customerFound.getEmail(),
                customerFound.getPhone(),
                customerFound.getUserAddress(),
                ""
        ));
        return "profile";
    }

    /**
     * Catalogo y acciones
     */

    @GetMapping("/catalogue")
    public String customerCatalogue(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "category", defaultValue = "0") Long category,
            Model model
    ) {
        Map<Long, String> categories = productTypeService.getAllProductTypes();
        model.addAttribute("categories", categories);//Lista de categorias para filtrado
        model.addAttribute("actual_category", category);//Categoria actual para la navegacion
        Page<Product> products = productService.findProductsForCustomers(page, category);
        model.addAttribute("page", products);
        return "/customer/catalogue";
    }

    @PostMapping("/add_to_shopping_cart")
    public String addToShoppingCart(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "category", defaultValue = "0") Long category,
            @RequestParam(name = "id", defaultValue = "0") Long id,
            @RequestParam(name = "quantity", defaultValue = "0") int quantity,
            RedirectAttributes redirectAttributes){
        System.out.println("Data get: page: " + page +
                "categoryID: " + category +
                "productID: " + id +
                "quantity: " + quantity);
        //Agregamos a mi shoppingkart y
        this.shoppingCart.addItem(id, quantity);
        System.out.println(shoppingCart.toString());
        //Al agregar a shoppingKart manda a mi redirect un valor para que no pierda su page y filter
        redirectAttributes.addAttribute("page", page);
        redirectAttributes.addAttribute("category",category);
        return "redirect:/customer/catalogue";
    }

    /**
     * ShoppingCart y acciones
     */

    @GetMapping("/shopping_cart")
    public String customerShoppingCart(
            @AuthenticationPrincipal User user,
            Model model
    ) {
        List<CartFractionDto> cartWithDetails = productService.getCartWithDetails(this.shoppingCart.getItems());
        cartWithDetails.forEach(item->{
            System.out.println(item.toString());
        });
        model.addAttribute("cart", cartWithDetails);
        model.addAttribute("name", (user.getName()+" "+user.getFirstName()));
        model.addAttribute("email", user.getEmail());
        return "/customer/shopping-kart";
    }

    @PostMapping("/create_order")
    public String customerCreateOrder(
            @AuthenticationPrincipal User user
    ){
        try {
            orderService.createOrder(user.getUsername(), this.shoppingCart.getItems());
            return "redirect:/customer/catalogue";
        } catch (Exception e) {
            System.out.println("Exception e: " + e.getMessage());
            return "redirect:/customer/shopping_cart";
        }
    }

    @GetMapping("/update_quantity")
    public String updateQuantity(
            @RequestParam(name= "id") Long id,
            @RequestParam(name = "action") String action
    ) {
        int maxStock= productService.findProductById(id).getStock();

        try {
            this.shoppingCart.updateQuantity(id,action,maxStock);
        } catch (Exception e) {
            System.out.println("Exception "+ e.getMessage());
        }

        return "redirect:/customer/shopping_cart";
    }

    @GetMapping("/remove_fsc")
    public String removeFromShoppingKart(
            @RequestParam(name = "id") Long id
    ) {
        this.shoppingCart.removeItem(id);
        return "redirect:/customer/shopping_cart";
    }

    /**
     *Orders y acciones
     */

    @GetMapping("/orders")
    public String customerOrders(
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @AuthenticationPrincipal User user,
            Model model
    ) {
        Page<Order> page = orderService.findUserOrders(pageNumber, user.getUsername());
        model.addAttribute("page", page);
        model.addAttribute("name", (user.getName()+" "+user.getFirstName()));
        model.addAttribute("email", user.getEmail());
        return "/customer/orders";
    }

    @GetMapping("/order_details")
    public String orderDetails(
            @RequestParam(name="id") Long id,
            @AuthenticationPrincipal User user,
            Model model
    ) {
        try {
            Order orderFound = orderService.getOrderByUser(id, user.getUsername());
            model.addAttribute("order", orderFound);
            model.addAttribute("name", user.getName()+" "+user.getFirstName());
            model.addAttribute("email", user.getEmail());
            return "/customer/order-details";
        } catch (Exception e) {
            System.out.println("Exception: "+e.getMessage());
            return "redirect:/customer/orders";
        }
    }
}
