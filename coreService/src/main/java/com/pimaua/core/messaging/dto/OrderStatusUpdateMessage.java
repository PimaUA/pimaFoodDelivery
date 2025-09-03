package com.pimaua.core.messaging.dto;

import com.pimaua.core.entity.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusUpdateMessage {
    private Integer orderId;
    private OrderStatus status;
}
