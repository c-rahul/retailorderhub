package com.training.retailorderhub.service.payment;

import org.springframework.stereotype.Component;

@Component
public class CreditCardPaymentStrategy implements PaymentStrategy {

    @Override
    public String paymentMethod() {
        return "CREDIT_CARD";
    }

    @Override
    public void pay(double amount) {
        System.out.println("Charging credit card: " + amount);
    }
}
