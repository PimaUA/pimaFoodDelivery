package com.pimaua.coreService.repository.orderServiceRepository;

import com.pimaua.coreService.entity.orderServiceEntity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    Optional<OrderItem> findByName(String name);
}
