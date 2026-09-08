package com.training.retailorderhub.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.training.retailorderhub.model.Order;
import com.training.retailorderhub.repository.OrderRepository;

@Service
public class OrderQueryService {

    private final OrderRepository orderRepository;

    public OrderQueryService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }
}
