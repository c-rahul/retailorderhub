package com.training.retailorderhub.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.training.retailorderhub.service.payment.PaymentStrategy;
import com.training.retailorderhub.service.payment.PaymentStrategyFactory;

@Service
public class OrderService {

    private final OrderValidator orderValidator;
    private final InventoryService inventoryService;
    private final OrderCreator orderCreator;
    private final PaymentStrategyFactory paymentStrategyFactory;

    public OrderService(OrderValidator orderValidator,
                        InventoryService inventoryService,
                        OrderCreator orderCreator,
                        PaymentStrategyFactory paymentStrategyFactory) {
        this.orderValidator = orderValidator;
        this.inventoryService = inventoryService;
        this.orderCreator = orderCreator;
        this.paymentStrategyFactory = paymentStrategyFactory;
    }

    @Transactional
    public boolean processOrder(String customerId, List<String> itemNames, String paymentMethod, double amount) {
        if (!validateCustomer(customerId) || !validateItems(itemNames)) {
            return false;
        }
        if (!inventoryService.hasStock(itemNames)) {
            return false;
        }

        PaymentStrategy paymentStrategy = paymentStrategyFactory.getStrategy(paymentMethod);
        if (paymentStrategy == null) {
            return false;
        }

        paymentStrategy.pay(amount);
        orderCreator.createAndSave(customerId, itemNames, paymentMethod, amount);
        inventoryService.decrement(itemNames);
        return true;
    }

    public boolean validateCustomer(String customerId) {
        return orderValidator.validateCustomer(customerId);
    }

    public boolean validateItems(List<String> itemNames) {
        return orderValidator.validateItems(itemNames);
    }
}
