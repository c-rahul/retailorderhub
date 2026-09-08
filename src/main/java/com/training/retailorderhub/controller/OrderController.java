package com.training.retailorderhub.controller;

import com.training.retailorderhub.service.OrderQueryService;
import com.training.retailorderhub.service.OrderService;
import com.training.retailorderhub.service.ProductCatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class OrderController {

    private final ProductCatalogService productCatalogService;
    private final OrderQueryService orderQueryService;
    private final OrderService orderService;

    public OrderController(ProductCatalogService productCatalogService,
                            OrderQueryService orderQueryService,
                            OrderService orderService) {
        this.productCatalogService = productCatalogService;
        this.orderQueryService = orderQueryService;
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("products", productCatalogService.findAll());
        return "index";
    }

    @PostMapping("/orders")
    public String placeOrder(@RequestParam String customerId,
                              @RequestParam String itemNames,
                              @RequestParam String paymentMethod,
                              @RequestParam double amount,
                              Model model) {
        List<String> items = Arrays.stream(itemNames.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        boolean success = orderService.processOrder(customerId, items, paymentMethod, amount);

        model.addAttribute("products", productCatalogService.findAll());
        model.addAttribute("orderSuccess", success);
        return "index";
    }

    @GetMapping("/orders")
    public String listOrders(Model model) {
        model.addAttribute("orders", orderQueryService.findAll());
        return "orders";
    }
}
