package com.pimaua.paymentService.entity.enums;

public enum PaymentStatus {
    REQUIRES_PAYMENT_METHOD, REQUIRES_CONFIRMATION, REQUIRES_ACTION, PROCESSING,
    SUCCEEDED, FAILED, CANCELED, REFUNDED;
}
