package com.training.retailorderhub.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.training.retailorderhub.model.Order;
import com.training.retailorderhub.repository.OrderRepository;

class OrderManagerTest {

    private EntityManager entityManager;
    private OrderRepository orderRepository;
    private OrderManager orderManager;

    @BeforeEach
    void setUp() {
        entityManager = mock(EntityManager.class);
        orderRepository = mock(OrderRepository.class);
        orderManager = new OrderManager(orderRepository);
        org.springframework.test.util.ReflectionTestUtils.setField(orderManager, "entityManager", entityManager);
    }

    @Test
    void rejectsMissingCustomer() {
        assertFalse(orderManager.processOrder(null, List.of("Laptop"), "PAYPAL", 10));
        assertFalse(orderManager.processOrder("", List.of("Laptop"), "PAYPAL", 10));
        verify(orderRepository, never()).save(org.mockito.ArgumentMatchers.any(Order.class));
    }

    @Test
    void rejectsMissingItems() {
        assertFalse(orderManager.processOrder("customer", null, "PAYPAL", 10));
        assertFalse(orderManager.processOrder("customer", List.of(), "PAYPAL", 10));
        verify(orderRepository, never()).save(org.mockito.ArgumentMatchers.any(Order.class));
    }

    @Test
    void rejectsOutOfStockItem() {
        stubInventory(0);

        assertFalse(orderManager.processOrder("customer", List.of("Laptop"), "PAYPAL", 10));
        verify(orderRepository, never()).save(org.mockito.ArgumentMatchers.any(Order.class));
    }

    @Test
    void treatsMissingInventoryRowAsOutOfStock() {
        Query inventoryQuery = mock(Query.class);
        when(entityManager.createNativeQuery(anyString())).thenReturn(inventoryQuery);
        when(inventoryQuery.getSingleResult()).thenThrow(new NoResultException());

        assertFalse(orderManager.processOrder("customer", List.of("Unknown"), "PAYPAL", 10));
        verify(orderRepository, never()).save(org.mockito.ArgumentMatchers.any(Order.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"CREDIT_CARD", "PAYPAL", "GIFT_CARD"})
    void processesSupportedPaymentMethods(String paymentMethod) {
        stubInventory(2);

        assertTrue(orderManager.processOrder("customer", List.of("Laptop"), paymentMethod, 10));
        verify(orderRepository).save(org.mockito.ArgumentMatchers.argThat(order ->
                "customer".equals(order.getCustomerId())
                        && "Laptop".equals(order.getItemNames())
                        && paymentMethod.equals(order.getPaymentMethod())
                        && order.getAmount() == 10
                        && "PENDING".equals(order.getStatus())
                        && order.getCreatedAt() != null));
    }

    @Test
    void rejectsUnknownPaymentMethod() {
        stubInventory(2);

        assertFalse(orderManager.processOrder("customer", List.of("Laptop"), "CASH", 10));
        verify(orderRepository, never()).save(org.mockito.ArgumentMatchers.any(Order.class));
    }

    @Test
    void updatesInventoryForEveryOrderedItem() {
        stubInventory(2, 1);

        assertTrue(orderManager.processOrder("customer", List.of("Laptop", "Mouse"), "PAYPAL", 25));
        verify(entityManager).createNativeQuery("UPDATE product SET quantity = quantity - 1 WHERE name = 'Laptop'");
        verify(entityManager).createNativeQuery("UPDATE product SET quantity = quantity - 1 WHERE name = 'Mouse'");
    }

    @Test
    void validationHelpersCoverValidAndInvalidInputs() {
        assertTrue(orderManager.validateCustomer("customer"));
        assertFalse(orderManager.validateCustomer(null));
        assertFalse(orderManager.validateCustomer(""));
        assertTrue(orderManager.validateItems(List.of("Laptop")));
        assertFalse(orderManager.validateItems(null));
        assertFalse(orderManager.validateItems(List.of()));
    }

    private void stubInventory(int... quantities) {
        AtomicInteger index = new AtomicInteger();
        when(entityManager.createNativeQuery(anyString())).thenAnswer(invocation -> {
            String sql = invocation.getArgument(0);
            Query query = mock(Query.class);
            if (sql.startsWith("SELECT")) {
                when(query.getSingleResult()).thenReturn(quantities[index.getAndIncrement()]);
            } else {
                when(query.executeUpdate()).thenReturn(1);
            }
            return query;
        });
    }
}
