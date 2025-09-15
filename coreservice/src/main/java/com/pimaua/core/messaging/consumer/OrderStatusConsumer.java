package com.pimaua.core.messaging.consumer;

import com.pimaua.core.entity.enums.OrderStatus;
import com.pimaua.core.exception.custom.notfound.OrderNotFoundException;
import com.pimaua.core.messaging.dto.OrderStatusUpdateMessage;
import com.pimaua.core.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class OrderStatusConsumer {
    private final OrderService orderService;
    private static final Logger logger = LoggerFactory.getLogger(OrderStatusConsumer.class);

    @Bean
    public Consumer<OrderStatusUpdateMessage> orderStatus() {
        return message -> {
            try {
                OrderStatus status = message.getStatus();
                orderService.updateOrderStatus(message.getOrderId(), status);
            } catch (IllegalArgumentException illegalArgumentException) {
                logger.warn("Invalid status '{}' in message: {}", message.getStatus(), message);
            } catch (OrderNotFoundException orderNotFoundException) {
                logger.error("Order not found for message: {}", message, orderNotFoundException);
                throw orderNotFoundException;
            } catch (Exception exception) {
                logger.error("Unexpected error while processing: {}", message, exception);
                throw exception;
            }
        };
    }
}

