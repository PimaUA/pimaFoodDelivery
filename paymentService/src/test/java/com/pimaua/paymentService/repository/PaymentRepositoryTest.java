package com.pimaua.paymentService.repository;

import com.pimaua.paymentService.entity.Payment;
import com.pimaua.paymentService.entity.enums.PaymentStatus;
import com.pimaua.paymentService.test.utils.BaseRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PaymentRepositoryTest extends BaseRepositoryTest {
    @Autowired
    PaymentRepository paymentRepository;

    @Test
    void saveAndFindPayment(){
        Payment payment=Payment.builder()
                .orderId(1)
                .userId(1)
                .amount(BigDecimal.valueOf(25.0))
                .currency("UAH")
                .paymentStatus(PaymentStatus.PROCESSING)
                .stripePaymentIntentId("1234")
                .createdAt(LocalDateTime.of(2025, 8, 1, 10, 30, 0))
                .build();

        Payment savedPayment=paymentRepository.save(payment);

        Optional<Payment>foundPayment=paymentRepository.findByOrderId(1);
        assertTrue(foundPayment.isPresent());
        assertEquals(1,foundPayment.get().getOrderId());
    }
}
