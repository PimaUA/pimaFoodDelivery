package com.pimaua.coreService.repository.userServiceRepository;

import com.pimaua.coreService.entity.userServiceEntity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Customer,Long> {
}
