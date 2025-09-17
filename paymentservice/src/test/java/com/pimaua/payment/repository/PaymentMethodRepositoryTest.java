package com.pimaua.payment.repository;

import com.pimaua.payment.entity.PaymentMethod;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
@Tag("integration")
public class PaymentMethodRepositoryTest{
    @Autowired
    PaymentMethodRepository paymentMethodRepository;

    @Container
    @ServiceConnection
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Test
    void saveAndFindPaymentMethod(){
        // Given create a PaymentMethod
        PaymentMethod paymentMethod= PaymentMethod.builder()
                .userId(1)
                .stripePaymentMethodId("123")
                .type("Credit Card")
                .brand("Visa")
                .expMonth(5)
                .expYear(2027)
                .createdAt(LocalDateTime.of(2025, 8, 1, 10, 30, 0))
                .build();

        // When: save the PaymentMethod to the repository
        paymentMethodRepository.save(paymentMethod);

        // Then: retrieve it by userId and verify it exists
        Optional<PaymentMethod>foundPaymentMethod=paymentMethodRepository.findByUserId(1);
        assertTrue(foundPaymentMethod.isPresent());
        assertEquals(1,foundPaymentMethod.get().getUserId());
    }
}
