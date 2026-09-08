package com.training.retailorderhub.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class OrderValidatorTest {

    private final OrderValidator validator = new OrderValidator();

    @Test
    void validatesCustomerPresence() {
        assertTrue(validator.validateCustomer("customer"));
        assertFalse(validator.validateCustomer(null));
        assertFalse(validator.validateCustomer(""));
    }

    @Test
    void validatesItemPresence() {
        assertTrue(validator.validateItems(List.of("Laptop")));
        assertFalse(validator.validateItems(null));
        assertFalse(validator.validateItems(List.of()));
    }
}
