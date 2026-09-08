package com.training.retailorderhub.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.training.retailorderhub.model.Order;
import com.training.retailorderhub.repository.OrderRepository;

class OrderCreatorTest {

    @Test
    void createsAndSavesPendingOrder() {
        OrderRepository repository = mock(OrderRepository.class);
        when(repository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        OrderCreator creator = new OrderCreator(repository);

        Order order = creator.createAndSave("customer", List.of("Laptop", "Mouse"), "PAYPAL", 25);

        assertEquals("customer", order.getCustomerId());
        assertEquals("Laptop,Mouse", order.getItemNames());
        assertEquals("PAYPAL", order.getPaymentMethod());
        assertEquals(25, order.getAmount());
        assertEquals("PENDING", order.getStatus());
        assertNotNull(order.getCreatedAt());
        verify(repository).save(order);
    }
}
