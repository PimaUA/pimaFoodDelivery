package com.pimaua.payment.repository;

import com.pimaua.payment.entity.PaymentMethod;
import com.pimaua.payment.test.utils.BaseRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PaymentMethodRepositoryTest extends BaseRepositoryTest {
    @Autowired
    PaymentMethodRepository paymentMethodRepository;

    @Test
    void saveAndFindPaymentMethod(){
        PaymentMethod paymentMethod= PaymentMethod.builder()
                .userId(1)
                .stripePaymentMethodId("123")
                .type("Credit Card")
                .brand("Visa")
                .expMonth(5)
                .expYear(2027)
                .createdAt(LocalDateTime.of(2025, 8, 1, 10, 30, 0))
                .build();

        paymentMethodRepository.save(paymentMethod);

        Optional<PaymentMethod>foundPaymentMethod=paymentMethodRepository.findByUserId(1);
        assertTrue(foundPaymentMethod.isPresent());
        assertEquals(1,foundPaymentMethod.get().getUserId());
    }
}
