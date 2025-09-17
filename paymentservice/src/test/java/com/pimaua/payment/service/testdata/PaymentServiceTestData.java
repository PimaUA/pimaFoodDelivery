package com.pimaua.payment.service.testdata;

import com.pimaua.payment.dto.OrderForPaymentDto;
import com.pimaua.payment.dto.PaymentCreateDto;
import com.pimaua.payment.entity.Payment;
import com.pimaua.payment.utils.enums.PaymentMethodType;
import com.pimaua.payment.utils.enums.PaymentStatus;

import java.math.BigDecimal;

public class PaymentServiceTestData {

    public static PaymentCreateDto mockPaymentCreateDto() {
        PaymentCreateDto dto = new PaymentCreateDto();
        dto.setAmount(BigDecimal.valueOf(50.00));
        dto.setCurrency("usd");
        dto.setPaymentMethodType(PaymentMethodType.CARD);
        dto.setPaymentMethodId("pm_12345");
        return dto;
    }

    public static OrderForPaymentDto mockOrderForPaymentDto() {
        OrderForPaymentDto dto = new OrderForPaymentDto();
        dto.setId(123);
        dto.setUserId(456);
        return dto;
    }

    public static Payment mockPayment(PaymentCreateDto requestDto, OrderForPaymentDto orderDto) {
        return Payment.builder()
                .id(1)
                .orderId(orderDto.getId())
                .userId(orderDto.getUserId())
                .amount(requestDto.getAmount())
                .currency(requestDto.getCurrency())
                .paymentStatus(PaymentStatus.SUCCEEDED)
                .paymentIntentId("pi_12345")
                .build();
    }
}
