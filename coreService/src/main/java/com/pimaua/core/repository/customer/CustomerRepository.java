package com.pimaua.core.repository.customer;

import com.pimaua.core.entity.customer.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Optional<Customer> findByPhoneNumber(String phoneNumber);
}
