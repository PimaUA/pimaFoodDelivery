package com.pimaua.payment.repository;

import com.pimaua.payment.entity.OutboxEvent;
import com.pimaua.payment.test.utils.BaseRepositoryTest;
import com.pimaua.payment.utils.enums.AggregateType;
import com.pimaua.payment.utils.enums.OrderStatus;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
public class OutboxEventRepositoryTest extends BaseRepositoryTest {
    @Autowired
    OutboxEventRepository outboxEventRepository;

    @Test
    void findByProcessedFalseTest() {
        // Given
        OutboxEvent outboxEvent = OutboxEvent.builder()
                .aggregateType(AggregateType.ORDER)
                .aggregateId("order-123")
                .orderStatus(OrderStatus.PENDING)
                .payload("{\"key\":\"value\"}")
                .processed(false) // explicitly unprocessed
                .retryCount(0)
                .build();

        // When
        OutboxEvent savedOutboxEvent = outboxEventRepository.save(outboxEvent);
        List<OutboxEvent> unprocessedEvents = outboxEventRepository.findByProcessedFalse();

        // Then
        assertNotNull(savedOutboxEvent.getId());
        assertEquals(1, unprocessedEvents.size());
        assertEquals(savedOutboxEvent.getId(), unprocessedEvents.get(0).getId());
        assertFalse(unprocessedEvents.get(0).isProcessed());
    }
}
