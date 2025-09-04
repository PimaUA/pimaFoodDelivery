package com.pimaua.core.repository.order.spec;

import com.pimaua.core.entity.enums.OrderStatus;
import com.pimaua.core.entity.order.Order;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class OrderSpecs {

    public static Specification<Order> hasUserId(Integer userId) {
        return (root, query, cb) -> cb.equal(root.get("userId"), userId);
    }

    public static Specification<Order> hasOrderStatus(OrderStatus orderStatus) {
        return (root, query, cb) ->
                orderStatus == null ? null : cb.equal(root.get("orderStatus"), orderStatus);
    }

    public static Specification<Order> createdFromTo(LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> cb.between(root.get("createdAt"), from, to);
    }
}
