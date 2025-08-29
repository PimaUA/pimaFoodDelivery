package com.pimaua.payment.mapper;

import com.stripe.model.Event;

public class OrderStatusMapper {

    private OrderStatusMapper() {
    }

    public static String mapStripeEventToOrderStatus(Event event) {
        return switch (event.getType()) {
            case "payment_intent.succeeded" -> "PAID";
            case "payment_intent.payment_failed" -> "PAYMENT_FAILED";
            default -> "UNKNOWN";
        };
    }
}
