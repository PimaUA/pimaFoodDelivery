package com.pimaua.payment.repository;

import com.pimaua.payment.entity.StripeCustomer;
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
public class StripeCustomerRepositoryTest{
    @Autowired
    StripeCustomerRepository stripeCustomerRepository;

    @Container
    @ServiceConnection
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

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
