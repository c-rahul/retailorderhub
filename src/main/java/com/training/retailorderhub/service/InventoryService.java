package com.training.retailorderhub.service;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean hasStock(List<String> itemNames) {
        for (String itemName : itemNames) {
            if (getInventoryQuantity(itemName) <= 0) {
                return false;
            }
        }
        return true;
    }

    public void decrement(List<String> itemNames) {
        for (String itemName : itemNames) {
            entityManager.createNativeQuery("UPDATE product SET quantity = quantity - 1 WHERE name = :itemName")
                    .setParameter("itemName", itemName)
                    .executeUpdate();
        }
    }

    private int getInventoryQuantity(String itemName) {
        try {
            Object result = entityManager.createNativeQuery("SELECT quantity FROM product WHERE name = :itemName")
                    .setParameter("itemName", itemName)
                    .getSingleResult();
            return ((Number) result).intValue();
        } catch (NoResultException e) {
            return 0;
        }
    }
}
