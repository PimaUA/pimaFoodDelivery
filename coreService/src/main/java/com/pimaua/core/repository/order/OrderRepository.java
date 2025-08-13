package com.pimaua.core.repository.order;

import com.pimaua.core.entity.enums.OrderStatus;
import com.pimaua.core.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findByOrderStatus(OrderStatus orderStatus);

    @Modifying
    @Query("DELETE FROM Order o WHERE o.id = :id")
    int deleteOrderById(@Param("id") Integer id);
}
