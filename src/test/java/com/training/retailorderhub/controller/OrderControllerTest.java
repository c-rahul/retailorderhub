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

import com.training.retailorderhub.service.OrderQueryService;
import com.training.retailorderhub.service.OrderService;
import com.training.retailorderhub.service.ProductCatalogService;

class OrderControllerTest {

    private ProductCatalogService productCatalogService;
    private OrderQueryService orderQueryService;
    private OrderService orderService;
    private OrderController controller;

    @BeforeEach
    void setUp() {
        productCatalogService = mock(ProductCatalogService.class);
        orderQueryService = mock(OrderQueryService.class);
        orderService = mock(OrderService.class);
        controller = new OrderController(productCatalogService, orderQueryService, orderService);
    }

    @Test
    void indexLoadsProducts() {
        when(productCatalogService.findAll()).thenReturn(List.of());
        Model model = new ExtendedModelMap();

        assertEquals("index", controller.index(model));
        assertEquals(List.of(), model.getAttribute("products"));
        verify(productCatalogService).findAll();
    }

    @Test
    void placeOrderTrimsItemsAndReportsSuccess() {
        when(orderService.processOrder("customer", List.of("Laptop", "Mouse"), "PAYPAL", 25)).thenReturn(true);
        when(productCatalogService.findAll()).thenReturn(List.of());
        Model model = new ExtendedModelMap();

        assertEquals("index", controller.placeOrder("customer", " Laptop, ,Mouse ", "PAYPAL", 25, model));
        assertEquals(true, model.getAttribute("orderSuccess"));
        verify(orderService).processOrder("customer", List.of("Laptop", "Mouse"), "PAYPAL", 25);
        verify(productCatalogService).findAll();
    }

    @Test
    void placeOrderReportsFailure() {
        when(orderService.processOrder("customer", List.of("Laptop"), "CASH", 25)).thenReturn(false);
        when(productCatalogService.findAll()).thenReturn(List.of());
        Model model = new ExtendedModelMap();

        assertEquals("index", controller.placeOrder("customer", "Laptop", "CASH", 25, model));
        assertEquals(false, model.getAttribute("orderSuccess"));
    }

    @Test
    void listOrdersLoadsOrders() {
        when(orderQueryService.findAll()).thenReturn(List.of());
        Model model = new ExtendedModelMap();

        assertEquals("orders", controller.listOrders(model));
        assertEquals(List.of(), model.getAttribute("orders"));
        verify(orderQueryService).findAll();
    }
}
