package com.pimaua.payment.dto;

import com.pimaua.payment.utils.enums.OrderStatus;
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
