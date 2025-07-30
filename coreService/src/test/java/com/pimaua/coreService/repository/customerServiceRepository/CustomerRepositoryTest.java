package com.pimaua.coreService.repository.customerServiceRepository;

import com.pimaua.coreService.entity.customerServiceEntity.Customer;
import com.pimaua.coreService.test.utils.BaseRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CustomerRepositoryTest extends BaseRepositoryTest {
    @Autowired
    private CustomerRepository userRepository;

    @Test
    void testSaveAndFindCustomer() {
        Customer customer = new Customer();
        customer.setUserId(2);
        customer.setName("John");
        customer.setPhoneNumber("123456");

        Customer savedCustomer = userRepository.save(customer);

        Optional<Customer> foundCustomer = userRepository.findByPhoneNumber("123456");
        assertTrue(foundCustomer.isPresent());
        assertEquals("John", foundCustomer.get().getName());
    }
}
