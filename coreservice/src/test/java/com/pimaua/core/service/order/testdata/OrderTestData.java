package com.pimaua.core.service.order.testdata;

import com.pimaua.core.dto.order.OrderCreateDto;
import com.pimaua.core.dto.order.OrderItemRequestDto;
import com.pimaua.core.dto.order.OrderResponseDto;
import com.pimaua.core.dto.order.OrderUpdateDto;
import com.pimaua.core.entity.enums.OrderStatus;
import com.pimaua.core.entity.order.Order;
import com.pimaua.core.entity.order.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderTestData {

    public static Order mockOrder() {
        return Order.builder()
                .id(1)
                .userId(100)
                .restaurantId(200)
                .orderStatus(OrderStatus.PENDING)
                .totalPrice(new BigDecimal("25.00"))
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static OrderResponseDto mockOrderResponseDto() {
        return OrderResponseDto.builder()
                .id(1)
                .userId(100)
                .restaurantId(200)
                .orderStatus(OrderStatus.PENDING)
                .totalPrice(new BigDecimal("25.00"))
                .build();
    }

    public static OrderItemRequestDto mockOrderItemRequestDto() {
        return OrderItemRequestDto.builder()
                .menuItemId(1)
                .quantity(2)
                .build();
    }

    public static OrderItem mockOrderItem() {
        return OrderItem.builder()
                .id(1)
                .menuItemId(1)
                .quantity(2)
                .unitPrice(new BigDecimal("12.50"))
                .totalPrice(new BigDecimal("25.00"))
                .build();
    }

    public static OrderCreateDto mockOrderCreateDto() {
        return OrderCreateDto.builder()
                .restaurantId(200)
                .pickupAddress("Pickup")
                .dropOffAddress("DropOff")
                .orderItems(List.of(mockOrderItemRequestDto()))
                .build();
    }

    public static OrderUpdateDto mockOrderUpdateDto() {
        return OrderUpdateDto.builder()
                .pickupAddress("New Address")
                .dropOffAddress("New Drop")
                .build();
    }
}
