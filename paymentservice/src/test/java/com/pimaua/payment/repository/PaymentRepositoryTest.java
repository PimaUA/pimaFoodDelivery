package com.pimaua.payment.repository;

import com.pimaua.payment.entity.Payment;
import com.pimaua.payment.utils.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
@Tag("integration")
public class PaymentRepositoryTest{
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

    @Container
    @ServiceConnection
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Test
    void saveAndFindPayment(){
        // When: try to find a Payment by orderId
        Optional<Payment> foundPayment = paymentRepository.findByOrderId(1);

        // Then: verify the Payment exists and has expected values
        assertTrue(foundPayment.isPresent());
        assertEquals(1, foundPayment.get().getOrderId());
        assertEquals(BigDecimal.valueOf(25.0), foundPayment.get().getAmount());
        assertEquals("UAH", foundPayment.get().getCurrency());
        assertEquals(PaymentStatus.PROCESSING, foundPayment.get().getPaymentStatus());
    }

    @Test
    void existsByOrderId_Success() {
        // When: check existence of Payment with orderId 1
        Boolean exists = paymentRepository.existsByOrderId(1);
        // Then: verify it exists
        assertTrue(exists);
    }

    @Test
    void existsByOrderId_Fault() {
        // Given: no Payment with orderId 999 exists
        // When: check existence of Payment with orderId 999
        Boolean exists = paymentRepository.existsByOrderId(999);

        // Then: verify it does not exist
        assertFalse(exists);
    }
}
