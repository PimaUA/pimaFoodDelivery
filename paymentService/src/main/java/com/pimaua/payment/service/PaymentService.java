package com.pimaua.payment.service;

import com.pimaua.payment.dto.PaymentCreateDto;
import com.pimaua.payment.entity.Payment;
import com.pimaua.payment.utils.enums.PaymentStatus;
import com.stripe.model.PaymentIntent;

public interface PaymentService {
    PaymentIntent createPaymentIntent(PaymentCreateDto paymentCreateDto, Integer orderId);

    Payment updatePaymentStatus(String paymentIntentId, PaymentStatus status);
}
