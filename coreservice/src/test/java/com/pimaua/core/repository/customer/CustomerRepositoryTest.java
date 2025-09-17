package com.pimaua.core.repository.customer;

import com.pimaua.core.entity.customer.Customer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
@Tag("integration")
public class CustomerRepositoryTest{
    @Autowired
    private CustomerRepository customerRepository;

    @Container
    @ServiceConnection
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Test
    void testSaveAndFindCustomer() {
        Customer customer = new Customer();
        customer.setUserId(2);
        customer.setName("John");
        customer.setPhoneNumber("123456");

        Customer savedCustomer = customerRepository.save(customer);

        // Test findByPhoneNumber
        Optional<Customer> foundCustomer = customerRepository.findByPhoneNumber("123456");
        assertTrue(foundCustomer.isPresent());
        assertEquals("John", foundCustomer.get().getName());

        // Test findByName
        Optional<Customer> foundByName = customerRepository.findByName("John");
        assertTrue(foundByName.isPresent());
        assertEquals("123456", foundByName.get().getPhoneNumber());
    }
}
