package com.pimaua.payment.repository;

import com.pimaua.payment.entity.OutboxEvent;
import com.pimaua.payment.utils.enums.AggregateType;
import com.pimaua.payment.utils.enums.OrderStatus;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
@Tag("integration")
public class OutboxEventRepositoryTest{
    @Autowired
    OutboxEventRepository outboxEventRepository;

    @Container
    @ServiceConnection
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Test
    void findByProcessedFalseTest() {
        // Given: create an unprocessed OutboxEvent
        OutboxEvent outboxEvent = OutboxEvent.builder()
                .aggregateType(AggregateType.ORDER)
                .aggregateId("order-123")
                .orderStatus(OrderStatus.PENDING)
                .payload("{\"key\":\"value\"}")
                .processed(false) // explicitly unprocessed
                .retryCount(0)
                .build();

        // When: save the event and fetch unprocessed events
        OutboxEvent savedOutboxEvent = outboxEventRepository.save(outboxEvent);
        List<OutboxEvent> unprocessedEvents = outboxEventRepository.findByProcessedFalse();

        // Then: verify the saved event is returned and marked as unprocessed
        assertNotNull(savedOutboxEvent.getId());
        assertEquals(1, unprocessedEvents.size());
        assertEquals(savedOutboxEvent.getId(), unprocessedEvents.get(0).getId());
        assertFalse(unprocessedEvents.get(0).isProcessed());
    }
}
