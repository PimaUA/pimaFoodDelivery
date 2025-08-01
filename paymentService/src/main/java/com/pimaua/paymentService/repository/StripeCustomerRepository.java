package com.pimaua.paymentService.repository;

import com.pimaua.paymentService.entity.StripeCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StripeCustomerRepository extends JpaRepository<StripeCustomer, Integer> {
    Optional<StripeCustomer> findByStripeCustomerId(String stripeCustomerId);
}
