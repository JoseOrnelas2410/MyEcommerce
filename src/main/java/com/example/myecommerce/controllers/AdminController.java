package com.example.myecommerce.controllers;

import com.example.myecommerce.config.StorageConfig;
import com.example.myecommerce.enums.StorageType;
import com.example.myecommerce.models.dto.AddProductDto;
import com.example.myecommerce.models.dto.PasswordUpdateDto;
import com.example.myecommerce.models.dto.UpdateProductDto;
import com.example.myecommerce.models.dto.UserUpdateDto;
import com.example.myecommerce.models.entity.Admin;
import com.example.myecommerce.models.entity.Order;
import com.example.myecommerce.models.entity.Product;
import com.example.myecommerce.models.entity.ProductType;
import com.example.myecommerce.services.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLOutput;
import java.util.Date;
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
    private final StorageService storageService;
    private final OrderService orderService;
    private final OrderStatusService orderStatusService;
    private final PaymentStatusService paymentStatusService;
    private final PdfService pdfService;

    /**
     * Profile
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
     *Catalogo y acciones
     */
    @GetMapping("/catalogue")
    public String adminCatalogue(
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model
    ){
        AddProductDto addProductDto = new AddProductDto();
        Map<Long,String> productTypeList= productTypeService.getAllProductTypes();
        Page<Product> productPage = productService.getAllProductsByPage(page);
        model.addAttribute("page", productPage);
        model.addAttribute("addProductDto", addProductDto);
        model.addAttribute("typeList", productTypeList);
        return "admin/catalogue";
    }

    @PostMapping("/add_product_type")
    public String addProductType(
            @ModelAttribute("typeDescription")String description
    ){
        productTypeService.addProductType(description);
        return "redirect:/admin/catalogue";
    }

    @PostMapping("/add_product")
    public String addProduct(
            @ModelAttribute("addProductDto")AddProductDto addProductDto,
            RedirectAttributes redirectAttributes
    ) {
        System.out.println("Producto obtenido"+addProductDto.toString());
        try {
            System.out.println("Guardando product");
            productService.saveProduct(addProductDto);
            System.out.print("Succes" + "Product saved");
        } catch (IOException e) {
            System.out.print("Error "+" Failed saving product"+ e.getMessage());
        }
        return "redirect:/admin/catalogue";

    }

    @PostMapping("/update_product")
    public String updateProduct(
            @ModelAttribute("updateProductData") UpdateProductDto updateProductDto
    ){
        System.out.println("Update data recived in:" + updateProductDto.toString());
        try {
            System.out.println("AdminController intentando hacer update de product");
            productService.updateProduct(updateProductDto);
        } catch (IOException e) {
            System.out.println("Error"+ e.getMessage());
            throw new RuntimeException("Error updating product" + e.getMessage());
        }
        return "redirect:/admin/catalogue";
    }
    /**
     *Orders y acciones
     */
    @GetMapping("/orders")
    public String adminOrders(
            @RequestParam(name = "page",defaultValue = "0") int pageNumber,
            @RequestParam(name = "order_status", defaultValue = "0") Long order_status,
            Model model
    ){
        Page<Order> page = orderService.getOrders(pageNumber, order_status);
        Map<Long,String> statusList = orderStatusService.getAllOrderStatus();
        model.addAttribute("page", page);
        model.addAttribute("status_list", statusList);
        model.addAttribute("order_status", order_status);
        return "admin/orders";
    }

    @GetMapping("/update_order")
    public String updateOrder(
            @RequestParam(name="id") Long id,
            Model model
    ){
        Map<Long, String> payment_status = paymentStatusService.findAll();
        model.addAttribute("payment_status", payment_status);
        Map<Long, String> order_status = orderStatusService.getAllOrderStatus();
        model.addAttribute("order_status", order_status);
        Order orderFound = orderService.getOrderById(id);
        model.addAttribute("order", orderFound);
        return "/admin/update-order";
    }

    @PostMapping("/update_order_status")
    public String updateOrderStatus(
            @RequestParam(name="id") Long id,
            @RequestParam(name="pay_status", defaultValue = "0") Long payment_status,
            @RequestParam(name ="ord_status", defaultValue = "0") Long order_status
    ){
        try {
            orderService.updateOrder(id, payment_status, order_status);
        } catch (Exception e) {
            System.out.println("Exception" + e.getMessage());
        }
        return "redirect:/admin/orders";
    }

    /**
     * Reportes
     */

    @GetMapping("/reports")
    public String adminReports(
            Model model,
            @RequestParam(name = "report_type", defaultValue = "0") int reportType,
            @RequestParam(name = "from", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam(name = "to", required = false)@DateTimeFormat(pattern = "yyyy-MM-dd") Date to
            ){
        try {
            Map<Integer, Object> reportData = orderService.createReport(reportType, from, to);
            model.addAttribute("report", reportData);
            model.addAttribute("actualReport", reportType);
            model.addAttribute("from_date", from);
            model.addAttribute("to_date", to);
        } catch (Exception e) {
            System.out.println("Exception e" + e.getMessage());
        }
        return "admin/reports";
    }

    @GetMapping("/reports/pdf")
    @ResponseBody
    public void adminReports(
            HttpServletResponse response,
            @RequestParam(name = "report", required = true) int reportType,
            @RequestParam(name = "from_date") Date from,
            @RequestParam(name = "to_date") Date to
    ) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=report.pdf");
        pdfService.generate(response.getOutputStream(), reportType,from,to);
    }
}
