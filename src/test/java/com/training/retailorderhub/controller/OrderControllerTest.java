package com.training.retailorderhub.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import com.training.retailorderhub.repository.OrderRepository;
import com.training.retailorderhub.repository.ProductRepository;
import com.training.retailorderhub.service.OrderManager;

class OrderControllerTest {

    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private OrderManager orderManager;
    private OrderController controller;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        orderRepository = mock(OrderRepository.class);
        orderManager = mock(OrderManager.class);
        controller = new OrderController(productRepository, orderRepository, orderManager);
    }

    @Test
    void indexLoadsProducts() {
        when(productRepository.findAll()).thenReturn(List.of());
        Model model = new ExtendedModelMap();

        assertEquals("index", controller.index(model));
        assertEquals(List.of(), model.getAttribute("products"));
        verify(productRepository).findAll();
    }

    @Test
    void placeOrderTrimsItemsAndReportsSuccess() {
        when(orderManager.processOrder("customer", List.of("Laptop", "Mouse"), "PAYPAL", 25)).thenReturn(true);
        when(productRepository.findAll()).thenReturn(List.of());
        Model model = new ExtendedModelMap();

        assertEquals("index", controller.placeOrder("customer", " Laptop, ,Mouse ", "PAYPAL", 25, model));
        assertEquals(true, model.getAttribute("orderSuccess"));
        verify(orderManager).processOrder("customer", List.of("Laptop", "Mouse"), "PAYPAL", 25);
        verify(productRepository).findAll();
    }

    @Test
    void placeOrderReportsFailure() {
        when(orderManager.processOrder("customer", List.of("Laptop"), "CASH", 25)).thenReturn(false);
        when(productRepository.findAll()).thenReturn(List.of());
        Model model = new ExtendedModelMap();

        assertEquals("index", controller.placeOrder("customer", "Laptop", "CASH", 25, model));
        assertEquals(false, model.getAttribute("orderSuccess"));
    }

    @Test
    void listOrdersLoadsOrders() {
        when(orderRepository.findAll()).thenReturn(List.of());
        Model model = new ExtendedModelMap();

        assertEquals("orders", controller.listOrders(model));
        assertEquals(List.of(), model.getAttribute("orders"));
        verify(orderRepository).findAll();
    }
}
