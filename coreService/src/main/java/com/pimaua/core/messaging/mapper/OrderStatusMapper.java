package com.pimaua.core.messaging.mapper;

import com.pimaua.core.entity.enums.OrderStatus;

public class OrderStatusMapper {

    private OrderStatusMapper() {
    }

    public static OrderStatus mapMessageStatusToOrderStatus(String status) {
        return switch (status) {
            case "PAID" -> OrderStatus.PAID;
            case "PAYMENT_FAILED" -> OrderStatus.PAYMENT_FAILED;
            default -> throw new IllegalArgumentException("Unknown status: " + status);
        };
    }
}
