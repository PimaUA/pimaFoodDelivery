package com.pimaua.core.service.order.testdata;

import com.pimaua.core.dto.order.OrderItemRequestDto;
import com.pimaua.core.dto.order.OrderItemResponseDto;
import com.pimaua.core.entity.enums.OrderStatus;
import com.pimaua.core.entity.order.Order;
import com.pimaua.core.entity.order.OrderItem;
import com.pimaua.core.entity.restaurant.MenuItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class OrderItemTestData {

    public static MenuItem mockMenuItem() {
        return MenuItem.builder()
                .id(1)
                .price(BigDecimal.valueOf(10.00))
                .name("Menu Item")
                .build();
    }

    public static Order mockOrder() {
        return Order.builder()
                .id(100)
                .userId(1)
                .restaurantId(2)
                .orderStatus(OrderStatus.PENDING)
                .totalPrice(BigDecimal.valueOf(20.00))
                .createdAt(LocalDateTime.of(2025, 7, 30, 12, 0))
                .pickupAddress("Some street")
                .pickupLatitude(BigDecimal.valueOf(40.1234))
                .pickupLongitude(BigDecimal.valueOf(40.1234))
                .dropOffAddress("Another street")
                .dropOffLatitude(BigDecimal.valueOf(50.1234))
                .dropOffLongitude(BigDecimal.valueOf(50.1234))
                .orderItems(new ArrayList<>())
                .build();
    }

    public static OrderItem mockOrderItem(Order order, MenuItem menuItem) {
        return OrderItem.builder()
                .id(1)
                .menuItemId(menuItem.getId())
                .name(menuItem.getName())
                .quantity(2)
                .unitPrice(menuItem.getPrice())
                .totalPrice(menuItem.getPrice().multiply(BigDecimal.valueOf(2)))
                .order(order)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static OrderItemRequestDto mockOrderItemRequestDto() {
        return OrderItemRequestDto.builder()
                .menuItemId(1)
                .quantity(2)
                .build();
    }

    public static OrderItemResponseDto mockOrderItemResponseDto() {
        return OrderItemResponseDto.builder()
                .id(1)
                .menuItemId(1)
                .name("Menu Item")
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(10.00))
                .totalPrice(BigDecimal.valueOf(20.00))
                .build();
    }

    public static OrderItem mockUpdatedOrderItem(Order order, MenuItem menuItem) {
        return OrderItem.builder()
                .id(1)
                .menuItemId(menuItem.getId())
                .name("Updated Item")
                .quantity(3)
                .unitPrice(BigDecimal.valueOf(12.00))
                .totalPrice(BigDecimal.valueOf(36.00))
                .order(order)
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
