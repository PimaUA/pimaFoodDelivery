package com.pimaua.payment.repository;

import com.pimaua.payment.entity.StripeCustomer;
import com.pimaua.payment.test.utils.BaseRepositoryTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("integration")
public class StripeCustomerRepositoryTest extends BaseRepositoryTest {
    @Autowired
    StripeCustomerRepository stripeCustomerRepository;

    @Test
    void saveAndFindStripeCustomer() {
        //given
        StripeCustomer stripeCustomer = StripeCustomer.builder()
                .userId(1)
                .stripeCustomerId("1234")
                .createdAt(LocalDateTime.of(2025, 8, 1, 10, 30, 0))
                .build();

        //when
        StripeCustomer savedStripeCustomer = stripeCustomerRepository.save(stripeCustomer);

        //then
        Optional<StripeCustomer> foundStripeCustomer = stripeCustomerRepository.findByStripeCustomerId("1234");
        assertTrue(foundStripeCustomer.isPresent());
        assertEquals("1234", foundStripeCustomer.get().getStripeCustomerId());
    }
}
