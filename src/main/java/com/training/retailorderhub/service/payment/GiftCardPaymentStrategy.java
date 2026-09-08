package com.training.retailorderhub.service.payment;

import org.springframework.stereotype.Component;

@Component
public class GiftCardPaymentStrategy implements PaymentStrategy {

    @Override
    public String paymentMethod() {
        return "GIFT_CARD";
    }

    @Override
    public void pay(double amount) {
        System.out.println("Charging gift card: " + amount);
    }
}
