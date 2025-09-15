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
        when(processedEventRepository.existsById(stripeEventId)).thenReturn(true);

        boolean result = processedEventService.isProcessed(stripeEventId);

        assertTrue(result);
        verify(processedEventRepository).existsById(stripeEventId);
    }

    @Test
    void isProcessed_ReturnsFalseWhenNotExists() {
        when(processedEventRepository.existsById(stripeEventId)).thenReturn(false);

        boolean result = processedEventService.isProcessed(stripeEventId);

        assertFalse(result);
        verify(processedEventRepository).existsById(stripeEventId);
    }

    @Test
    void markProcessed_SavesProcessedEvent() {
        processedEventService.markProcessed(stripeEventId);

        verify(processedEventRepository).save(argThat(event ->
                event.getId().equals(stripeEventId)
        ));
    }
}
