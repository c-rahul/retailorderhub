package com.training.retailorderhub.controller;

import com.training.retailorderhub.repository.OrderRepository;
import com.training.retailorderhub.repository.ProductRepository;
import com.training.retailorderhub.service.OrderService;
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

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    public OrderController(ProductRepository productRepository,
                            OrderRepository orderRepository,
                            OrderService orderService) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("products", productRepository.findAll());
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

        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("orderSuccess", success);
        return "index";
    }

    @GetMapping("/orders")
    public String listOrders(Model model) {
        model.addAttribute("orders", orderRepository.findAll());
        return "orders";
    }
}
