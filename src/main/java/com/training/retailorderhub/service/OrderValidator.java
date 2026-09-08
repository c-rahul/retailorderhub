package com.training.retailorderhub.service;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    public boolean validateCustomer(String customerId) {
        return customerId != null && !customerId.isEmpty();
    }

    public boolean validateItems(List<String> itemNames) {
        return itemNames != null && !itemNames.isEmpty();
    }
}
