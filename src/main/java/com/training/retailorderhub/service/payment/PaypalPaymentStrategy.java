package com.training.retailorderhub.service.payment;

import org.springframework.stereotype.Component;

@Component
public class PaypalPaymentStrategy implements PaymentStrategy {

    @Override
    public String paymentMethod() {
        return "PAYPAL";
    }

    @Override
    public void pay(double amount) {
        System.out.println("Charging PayPal: " + amount);
    }
}
