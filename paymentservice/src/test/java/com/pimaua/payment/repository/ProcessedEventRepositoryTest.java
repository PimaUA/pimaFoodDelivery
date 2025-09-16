package com.pimaua.payment.repository;

import com.pimaua.payment.entity.ProcessedEvent;
import com.pimaua.payment.test.utils.BaseRepositoryTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@Tag("integration")
public class ProcessedEventRepositoryTest extends BaseRepositoryTest {
    @Autowired
    ProcessedEventRepository processedEventRepository;

    @Test
    void saveProcessedEvent_Success(){
        //given
        ProcessedEvent processedEvent = ProcessedEvent.builder()
                .id("12345")
                .build();

        //when
        ProcessedEvent savedProcessedEvent =processedEventRepository.save(processedEvent);

        //then
        assertNotNull(savedProcessedEvent.getId());
        assertEquals("12345", savedProcessedEvent.getId());
        boolean exists = processedEventRepository.existsById("12345");
        assertTrue(exists);
    }
}
