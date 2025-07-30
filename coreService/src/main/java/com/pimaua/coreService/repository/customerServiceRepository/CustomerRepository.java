package com.pimaua.coreService.repository.customerServiceRepository;

import com.pimaua.coreService.entity.customerServiceEntity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {
    Optional<Customer> findByPhoneNumber(String phoneNumber);
}
