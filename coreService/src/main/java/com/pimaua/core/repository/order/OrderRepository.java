package com.pimaua.core.repository.order;

import com.pimaua.core.entity.enums.OrderStatus;
import com.pimaua.core.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findByOrderStatus(OrderStatus orderStatus);
}
