package com.pimaua.payment.service.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pimaua.payment.entity.OutboxEvent;
import com.pimaua.payment.utils.enums.AggregateType;
import com.pimaua.payment.utils.enums.OrderStatus;

public class OutboxEventFactory {

    private OutboxEventFactory() {
        // utility class
    }

    public static OutboxEvent createOrderOutboxEvent(Integer orderId, Object message,
                                                     OrderStatus orderStatus, ObjectMapper objectMapper) {
        try {
            return OutboxEvent.builder()
                    .aggregateType(AggregateType.ORDER)
                    .aggregateId(orderId.toString())
                    .orderStatus(orderStatus)
                    .payload(objectMapper.writeValueAsString(message))
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize payload", e);
        }
    }
}
