package com.pimaua.core.entity.enums;

import java.util.Collections;
import java.util.Set;

public enum OrderStatus {
    PENDING,
    CONFIRMED,
    IN_PROGRESS,
    DELIVERED,
    PAID,
    PAYMENT_FAILED,
    CANCELLED;

    private Set<OrderStatus> allowedNextStatus;

    static {
        PENDING.allowedNextStatus = Set.of(CONFIRMED, CANCELLED);
        CONFIRMED.allowedNextStatus = Set.of(IN_PROGRESS, CANCELLED);
        IN_PROGRESS.allowedNextStatus = Set.of(DELIVERED, CANCELLED);
        DELIVERED.allowedNextStatus = Set.of(PAID, PAYMENT_FAILED);
        PAID.allowedNextStatus = Set.of();
        PAYMENT_FAILED.allowedNextStatus = Set.of();
        CANCELLED.allowedNextStatus = Set.of();
    }

    public boolean canTransitionTo(OrderStatus nextStatus) {
        return allowedNextStatus.contains(nextStatus);
    }

    public Set<OrderStatus> getAllowedNextStatus() {
        return Collections.unmodifiableSet(allowedNextStatus);
    }
}
