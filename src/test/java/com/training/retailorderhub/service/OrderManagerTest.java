package com.training.retailorderhub.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.training.retailorderhub.service.payment.PaymentStrategy;
import com.training.retailorderhub.service.payment.PaymentStrategyFactory;

class OrderManagerTest {

    private OrderValidator orderValidator;
    private InventoryService inventoryService;
    private OrderCreator orderCreator;
    private PaymentStrategyFactory paymentStrategyFactory;
    private PaymentStrategy paymentStrategy;
    private OrderManager orderManager;

    @BeforeEach
    void setUp() {
        orderValidator = mock(OrderValidator.class);
        inventoryService = mock(InventoryService.class);
        orderCreator = mock(OrderCreator.class);
        paymentStrategyFactory = mock(PaymentStrategyFactory.class);
        paymentStrategy = mock(PaymentStrategy.class);
        orderManager = new OrderManager(orderValidator, inventoryService, orderCreator, paymentStrategyFactory);
    }

    @Test
    void rejectsInvalidCustomerBeforeCheckingItems() {
        when(orderValidator.validateCustomer(null)).thenReturn(false);

        assertFalse(orderManager.processOrder(null, List.of("Laptop"), "PAYPAL", 10));
        verify(orderValidator, never()).validateItems(anyList());
        verify(inventoryService, never()).hasStock(anyList());
    }

    @Test
    void rejectsInvalidItems() {
        when(orderValidator.validateCustomer("customer")).thenReturn(true);
        when(orderValidator.validateItems(List.of())).thenReturn(false);

        assertFalse(orderManager.processOrder("customer", List.of(), "PAYPAL", 10));
        verify(inventoryService, never()).hasStock(anyList());
    }

    @Test
    void rejectsOutOfStockOrder() {
        List<String> items = List.of("Laptop");
        when(orderValidator.validateCustomer("customer")).thenReturn(true);
        when(orderValidator.validateItems(items)).thenReturn(true);
        when(inventoryService.hasStock(items)).thenReturn(false);

        assertFalse(orderManager.processOrder("customer", items, "PAYPAL", 10));
        verify(paymentStrategyFactory, never()).getStrategy("PAYPAL");
    }

    @Test
    void rejectsUnknownPaymentMethod() {
        List<String> items = List.of("Laptop");
        allowOrder(items);
        when(paymentStrategyFactory.getStrategy("CASH")).thenReturn(null);

        assertFalse(orderManager.processOrder("customer", items, "CASH", 10));
        verify(orderCreator, never()).createAndSave("customer", items, "CASH", 10);
    }

    @Test
    void processesValidOrderThroughCollaborators() {
        List<String> items = List.of("Laptop", "Mouse");
        allowOrder(items);
        when(paymentStrategyFactory.getStrategy("PAYPAL")).thenReturn(paymentStrategy);

        assertTrue(orderManager.processOrder("customer", items, "PAYPAL", 25));
        verify(paymentStrategy).pay(25);
        verify(orderCreator).createAndSave("customer", items, "PAYPAL", 25);
        verify(inventoryService).decrement(items);
    }

    @Test
    void delegatesValidationHelpers() {
        when(orderValidator.validateCustomer("customer")).thenReturn(true);
        when(orderValidator.validateItems(List.of("Laptop"))).thenReturn(true);

        assertTrue(orderManager.validateCustomer("customer"));
        assertTrue(orderManager.validateItems(List.of("Laptop")));
    }

    private void allowOrder(List<String> items) {
        when(orderValidator.validateCustomer("customer")).thenReturn(true);
        when(orderValidator.validateItems(items)).thenReturn(true);
        when(inventoryService.hasStock(items)).thenReturn(true);
    }
}
