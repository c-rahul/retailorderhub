package com.training.retailorderhub.service.payment;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class PaymentStrategyFactory {

    private final Map<String, PaymentStrategy> strategies;

    public PaymentStrategyFactory(List<PaymentStrategy> paymentStrategies) {
        strategies = paymentStrategies.stream()
                .collect(Collectors.toUnmodifiableMap(PaymentStrategy::paymentMethod, Function.identity()));
    }

    public PaymentStrategy getStrategy(String paymentMethod) {
        return strategies.get(paymentMethod);
    }
}
