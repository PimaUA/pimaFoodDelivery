package com.pimaua.payment.repository;

import com.pimaua.payment.entity.Payment;
import com.pimaua.payment.utils.enums.PaymentStatus;
import com.pimaua.payment.test.utils.BaseRepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentRepositoryTest extends BaseRepositoryTest {
    @Autowired
    PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        // Create and save a default payment for tests
        Payment defaultPayment = Payment.builder()
                .orderId(1)
                .userId(1)
                .amount(BigDecimal.valueOf(25.0))
                .currency("UAH")
                .paymentStatus(PaymentStatus.PROCESSING)
                .paymentIntentId("1234")
                .createdAt(LocalDateTime.of(2025, 8, 1, 10, 30, 0))
                .build();

        paymentRepository.save(defaultPayment);
    }

    @Test
    void saveAndFindPayment(){
        Optional<Payment> foundPayment = paymentRepository.findByOrderId(1);
        assertTrue(foundPayment.isPresent());
        assertEquals(1, foundPayment.get().getOrderId());
        assertEquals(BigDecimal.valueOf(25.0), foundPayment.get().getAmount());
        assertEquals("UAH", foundPayment.get().getCurrency());
        assertEquals(PaymentStatus.PROCESSING, foundPayment.get().getPaymentStatus());
    }

    @Test
    void existsByOrderId_Success() {
        Boolean exists = paymentRepository.existsByOrderId(1);
        assertTrue(exists);
    }

    @Test
    void existsByOrderId_Fault() {
        // Given - no payment with orderId 999 exists
        // When
        Boolean exists = paymentRepository.existsByOrderId(999);
        // Then
        assertFalse(exists);
    }
}
