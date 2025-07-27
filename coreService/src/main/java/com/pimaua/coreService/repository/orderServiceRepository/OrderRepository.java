package com.pimaua.coreService.repository.orderServiceRepository;

import com.pimaua.coreService.entity.orderServiceEntity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {
}
