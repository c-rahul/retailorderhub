package com.training.retailorderhub.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class ModelTest {

    @Test
    void productAccessorsRoundTripValues() {
        Product product = new Product();
        product.setId(7L);
        product.setName("Laptop");
        product.setPrice(1200.50);
        product.setQuantity(4);

        assertEquals(7L, product.getId());
        assertEquals("Laptop", product.getName());
        assertEquals(1200.50, product.getPrice());
        assertEquals(4, product.getQuantity());
    }

    @Test
    void orderAccessorsRoundTripValues() {
        LocalDateTime createdAt = LocalDateTime.of(2026, 9, 8, 12, 30);
        Order order = new Order();
        order.setId(9L);
        order.setCustomerId("customer-1");
        order.setItemNames("Laptop,Mouse");
        order.setPaymentMethod("PAYPAL");
        order.setAmount(1250.0);
        order.setStatus("PENDING");
        order.setCreatedAt(createdAt);

        assertEquals(9L, order.getId());
        assertEquals("customer-1", order.getCustomerId());
        assertEquals("Laptop,Mouse", order.getItemNames());
        assertEquals("PAYPAL", order.getPaymentMethod());
        assertEquals(1250.0, order.getAmount());
        assertEquals("PENDING", order.getStatus());
        assertEquals(createdAt, order.getCreatedAt());
    }
}
