package com.example.myecommerce.controllers;

import com.example.myecommerce.models.dto.CartFractionDto;
import com.example.myecommerce.models.dto.PasswordUpdateDto;
import com.example.myecommerce.models.dto.UserUpdateDto;
import com.example.myecommerce.models.entity.*;
import com.example.myecommerce.services.*;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/customer")
@PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
@RequiredArgsConstructor
public class CustomerController {

    //private final ShoppingCart shoppingCart;
    private final CartService cartService;
    private final UserService userService;
    private final ProductService productService;
    private final ProductTypeService productTypeService;
    private final OrderService orderService;

    @Value("${stripe.key.public}")
    private String stripePublicKey;

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
            @RequestParam(name = "filter_value", defaultValue = "0") Long filter_value,
            Model model
    ) {
        Map<Long, String> categories = productTypeService.getAllProductTypes();
        model.addAttribute("categories", categories);//Lista de categorias para filtrado
        model.addAttribute("filter_value", filter_value);//Categoria actual para la navegacion
        Page<Product> products = productService.findProductsForCustomers(page, filter_value);
        model.addAttribute("page", products);
        return "customer/catalogue";
    }

    @PostMapping("/add_to_shopping_cart")
    public String addToShoppingCart(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "filter_value", defaultValue = "0") Long filter_value,
            @RequestParam(name = "id", defaultValue = "0") Long id,
            @RequestParam(name = "quantity", defaultValue = "0") int quantity,
            RedirectAttributes redirectAttributes){
        String response = cartService.addProduct(id,quantity);
        redirectAttributes.addFlashAttribute("success",response);
        System.out.println("Post add to cart \nPage of request"+page);
        System.out.println("filter of request"+filter_value);
        redirectAttributes.addAttribute("page",page);
        redirectAttributes.addAttribute("filter_value",filter_value);
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
        List<CartFractionDto> cartWithDetails = productService.getCartWithDetails(cartService.completeCart());
        BigDecimal cartTotal = productService.getCartTotal(cartService.completeCart());
        model.addAttribute("cart", cartWithDetails);
        model.addAttribute("total", cartTotal);
        model.addAttribute("name", (user.getName()+" "+user.getFirstName()));
        model.addAttribute("email", user.getEmail());
        return "customer/shopping-kart";
    }

    @GetMapping("/update_quantity")
    public String updateQuantity(
            @RequestParam(name= "id") Long id,
            @RequestParam(name = "action") String action,
            RedirectAttributes redirectAttributes
    ) {
        String response = cartService.updateQuantity(id, action);
        redirectAttributes.addFlashAttribute("success",response);
        return "redirect:/customer/shopping_cart";
    }

    @GetMapping("/remove_fsc")
    public String removeFromShoppingKart(
            @RequestParam(name = "id") Long id,
            RedirectAttributes redirectAttributes
    ) {
        cartService.removeProduct(id);
        redirectAttributes.addFlashAttribute("success","Item Removed");
        return "redirect:/customer/shopping_cart";
    }

    @PostMapping("/create_order")
    public String customerCreateOrder(
            @AuthenticationPrincipal User user,
            RedirectAttributes redirectAttributes
    ){
        String id = orderService.createOrder(user.getUsername());
        redirectAttributes.addFlashAttribute("success","Order Created");
        return "redirect:/customer/order_details?id="+id;
    }

    @PostMapping("/shopping_cart")
    public String payOrder(
            @AuthenticationPrincipal User user,
            Model model
    ) throws StripeException, IllegalAccessException {
        Long orderId = orderService.startPayment(user.getUsername());
        return "redirect:/customer/pay?id="+orderId;
    }

    /**
     * Payment
     */

    @GetMapping("/pay")
    public String payOrder(
            @AuthenticationPrincipal User user,
            @RequestParam(name = "id", defaultValue = "0") Long id,
            Model model
    ) throws IllegalAccessException {
        System.out.println("pay endpoint started for customer "+ user.getUsername());
        String clientSecret = orderService.getClientSecret(user.getUsername(), id);
        System.out.println("cleint secret gotten from db " + clientSecret);
        model.addAttribute("public_key", this.stripePublicKey);
        model.addAttribute("client_secret", clientSecret);
        return "customer/pay";
    }

    /**
     *Orders y acciones
     */

    @GetMapping("/orders")
    public String customerOrders(
            @RequestParam(name = "page", defaultValue = "0") int pageNumber,
            @RequestParam(name = "filter_value", defaultValue = "0") Long filter_value,
            @AuthenticationPrincipal User user,
            Model model
    ) {
        Page<Order> page = orderService.findUserOrders(pageNumber, user.getUsername());
        model.addAttribute("page", page);
        model.addAttribute("name", (user.getName()+" "+user.getFirstName()));
        model.addAttribute("email", user.getEmail());
        model.addAttribute("filter_value",filter_value);
        return "customer/orders";
    }

    @GetMapping("/order_details")
    public String orderDetails(
            @RequestParam(name="id") Long id,
            @AuthenticationPrincipal User user,
            Model model
    ) throws IllegalAccessException {
        Order orderFound = orderService.getOrderByUser(id, user.getUsername());
        model.addAttribute("order", orderFound);
        model.addAttribute("name", user.getName()+" "+user.getFirstName());
        model.addAttribute("email", user.getEmail());
        return "customer/order-details";
    }
}
