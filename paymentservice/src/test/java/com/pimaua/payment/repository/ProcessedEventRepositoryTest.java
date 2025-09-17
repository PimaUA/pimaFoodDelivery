package com.pimaua.payment.repository;

import com.pimaua.payment.entity.ProcessedEvent;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
@Tag("integration")
public class ProcessedEventRepositoryTest{
    @Autowired
    ProcessedEventRepository processedEventRepository;

    @Container
    @ServiceConnection
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Test
    void saveProcessedEvent_Success(){
        // Given:  create a ProcessedEvent with a specific ID
        ProcessedEvent processedEvent = ProcessedEvent.builder()
                .id("12345")
                .build();

        // When: save the ProcessedEvent
        ProcessedEvent savedProcessedEvent =processedEventRepository.save(processedEvent);

        // Then: verify it was saved and can be found by ID
        assertNotNull(savedProcessedEvent.getId());
        assertEquals("12345", savedProcessedEvent.getId());
        boolean exists = processedEventRepository.existsById("12345");
        assertTrue(exists);
    }
}
