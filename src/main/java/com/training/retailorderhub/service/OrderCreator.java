package com.training.retailorderhub.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.training.retailorderhub.model.Order;
import com.training.retailorderhub.repository.OrderRepository;

@Service
public class OrderCreator {

    private final OrderRepository orderRepository;

    public OrderCreator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order createAndSave(String customerId, List<String> itemNames, String paymentMethod, double amount) {
        Order order = new Order();
        order.setCustomerId(customerId);
        order.setItemNames(String.join(",", itemNames));
        order.setPaymentMethod(paymentMethod);
        order.setAmount(amount);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }
}
