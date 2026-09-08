package com.training.retailorderhub.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class InventoryServiceTest {

    private EntityManager entityManager;
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        entityManager = mock(EntityManager.class);
        inventoryService = new InventoryService();
        ReflectionTestUtils.setField(inventoryService, "entityManager", entityManager);
    }

    @Test
    void reportsStockForAllItems() {
        stubInventory(2, 1);

        assertTrue(inventoryService.hasStock(List.of("Laptop", "Mouse")));
    }

    @Test
    void reportsNoStockWhenAnyItemIsEmpty() {
        stubInventory(2, 0);

        assertFalse(inventoryService.hasStock(List.of("Laptop", "Mouse")));
    }

    @Test
    void treatsMissingItemAsEmptyStock() {
        Query query = mock(Query.class);
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);
        when(query.getSingleResult()).thenThrow(new NoResultException());

        assertFalse(inventoryService.hasStock(List.of("Unknown")));
    }

    @Test
    void emptyInventoryRequestHasStock() {
        assertTrue(inventoryService.hasStock(List.of()));
    }

    @Test
    void decrementsEveryItem() {
        Query query = mock(Query.class);
        when(entityManager.createNativeQuery(anyString())).thenReturn(query);

        inventoryService.decrement(List.of("Laptop", "Mouse"));

        verify(entityManager).createNativeQuery("UPDATE product SET quantity = quantity - 1 WHERE name = 'Laptop'");
        verify(entityManager).createNativeQuery("UPDATE product SET quantity = quantity - 1 WHERE name = 'Mouse'");
        verify(query, org.mockito.Mockito.times(2)).executeUpdate();
    }

    private void stubInventory(int... quantities) {
        AtomicInteger index = new AtomicInteger();
        when(entityManager.createNativeQuery(anyString())).thenAnswer(invocation -> {
            Query query = mock(Query.class);
            when(query.getSingleResult()).thenReturn(quantities[index.getAndIncrement()]);
            return query;
        });
    }
}
