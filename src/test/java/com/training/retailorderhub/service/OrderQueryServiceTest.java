package com.training.retailorderhub.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.training.retailorderhub.model.Order;
import com.training.retailorderhub.repository.OrderRepository;

class OrderQueryServiceTest {

    @Test
    void returnsAllOrdersFromRepository() {
        OrderRepository repository = mock(OrderRepository.class);
        List<Order> orders = List.of(new Order());
        when(repository.findAll()).thenReturn(orders);
        OrderQueryService service = new OrderQueryService(repository);

        assertEquals(orders, service.findAll());
        verify(repository).findAll();
    }
}
