package com.pimaua.core.repository.customer;

import com.pimaua.core.entity.customer.Customer;
import com.pimaua.core.test.utils.BaseRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomerRepositoryTest extends BaseRepositoryTest {
    @Autowired
    private CustomerRepository customerRepository;

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
