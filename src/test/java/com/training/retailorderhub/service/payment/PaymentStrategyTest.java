package com.training.retailorderhub.service.payment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.jupiter.api.Test;

class PaymentStrategyTest {

    @Test
    void strategiesDeclarePaymentMethodsAndCanCharge() {
        List<PaymentStrategy> strategies = List.of(
                new CreditCardPaymentStrategy(),
                new PaypalPaymentStrategy(),
                new GiftCardPaymentStrategy());
        PaymentStrategyFactory factory = new PaymentStrategyFactory(strategies);

        assertEquals("CREDIT_CARD", strategies.get(0).paymentMethod());
        assertEquals("PAYPAL", strategies.get(1).paymentMethod());
        assertEquals("GIFT_CARD", strategies.get(2).paymentMethod());
        strategies.forEach(strategy -> strategy.pay(10));
        assertEquals(strategies.get(0), factory.getStrategy("CREDIT_CARD"));
        assertEquals(strategies.get(1), factory.getStrategy("PAYPAL"));
        assertEquals(strategies.get(2), factory.getStrategy("GIFT_CARD"));
        assertNull(factory.getStrategy("CASH"));
    }

    @Test
    void factoryAcceptsStrategyImplementations() {
        PaymentStrategy strategy = mock(PaymentStrategy.class);
        org.mockito.Mockito.when(strategy.paymentMethod()).thenReturn("CUSTOM");

        PaymentStrategyFactory factory = new PaymentStrategyFactory(List.of(strategy));

        assertEquals(strategy, factory.getStrategy("CUSTOM"));
    }
}
