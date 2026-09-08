package com.training.retailorderhub.service.payment;

public interface PaymentStrategy {

    String paymentMethod();

    void pay(double amount);
}
