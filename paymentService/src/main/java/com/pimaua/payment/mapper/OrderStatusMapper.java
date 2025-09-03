package com.pimaua.payment.mapper;

import com.pimaua.payment.utils.enums.OrderStatus;
import com.pimaua.payment.utils.enums.PaymentStatus;
import com.stripe.model.Event;

public class OrderStatusMapper {

    private OrderStatusMapper() {
    }

    public static OrderStatus mapStripeEventToOrderStatus(Event event) {
        return switch (event.getType()) {
            case "payment_intent.succeeded" -> OrderStatus.PAID;
            case "payment_intent.payment_failed" -> OrderStatus.PAYMENT_FAILED;
            default -> OrderStatus.UNKNOWN;
        };
    }

    public static OrderStatus mapPaymentStatusToOrderStatus(PaymentStatus paymentStatus) {
        if (paymentStatus == null) {
            throw new IllegalArgumentException("Payment status cannot be null");
        }
        return switch (paymentStatus) {
            case SUCCEEDED -> OrderStatus.PAID;
            case FAILED -> OrderStatus.PAYMENT_FAILED;
            default -> OrderStatus.UNKNOWN;
        };
    }
}
