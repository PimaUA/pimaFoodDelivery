package com.pimaua.payment.service.events;

import com.pimaua.payment.entity.ProcessedEvent;
import com.pimaua.payment.repository.ProcessedEventRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcessedEventService {
    private final ProcessedEventRepository processedEventRepository;

    @Transactional(readOnly = true)
    public boolean isProcessed(String stripeEventId) {
        return processedEventRepository.existsById(stripeEventId);
    }

    @Transactional
    public void markProcessed(String stripeEventId) {
        processedEventRepository.save(new ProcessedEvent(stripeEventId));
    }
}
