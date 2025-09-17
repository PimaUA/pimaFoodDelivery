package com.pimaua.payment.service;

import com.pimaua.payment.repository.ProcessedEventRepository;
import com.pimaua.payment.service.events.ProcessedEventService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class ProcessedEventServiceTest {

    @Mock
    private ProcessedEventRepository processedEventRepository;

    @InjectMocks
    private ProcessedEventService processedEventService;

    private final String stripeEventId = "evt_12345";

    @Test
    void isProcessed_ReturnsTrueWhenExists() {
        // given: the event ID already exists in the repository
        when(processedEventRepository.existsById(stripeEventId)).thenReturn(true);

        // when: checking if the event was processed
        boolean result = processedEventService.isProcessed(stripeEventId);

        // then: result is true and repository was queried
        assertTrue(result);
        verify(processedEventRepository).existsById(stripeEventId);
    }

    @Test
    void isProcessed_ReturnsFalseWhenNotExists() {
        // given: the event ID does not exist in the repository
        when(processedEventRepository.existsById(stripeEventId)).thenReturn(false);

        // when: checking if the event was processed
        boolean result = processedEventService.isProcessed(stripeEventId);

        // then: result is false and repository was queried
        assertFalse(result);
        verify(processedEventRepository).existsById(stripeEventId);
    }

    @Test
    void markProcessed_SavesProcessedEvent() {
        // given: a new stripeEventId that has not been marked yet

        // when: marking the event as processed
        processedEventService.markProcessed(stripeEventId);

        // then: repository should save a new ProcessedEvent with matching ID
        verify(processedEventRepository).save(argThat(event ->
                event.getId().equals(stripeEventId)
        ));
    }
}
