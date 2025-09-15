package com.pimaua.payment.service.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pimaua.payment.dto.OrderStatusUpdateMessage;
import com.pimaua.payment.entity.DeadLetterEvent;
import com.pimaua.payment.entity.OutboxEvent;
import com.pimaua.payment.repository.DeadLetterRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import com.pimaua.payment.repository.OutboxEventRepository;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Getter
@Setter
@AllArgsConstructor
public class OutboxEventPublisher {
    private final OutboxEventRepository outboxEventRepository;
    private final DeadLetterRepository deadLetterRepository;
    private final StreamBridge streamBridge;
    private final ObjectMapper objectMapper;
    private final static Logger logger = LoggerFactory.getLogger(OutboxEventPublisher.class);
    private static final int MAX_RETRY_COUNT = 5;

    @Scheduled(fixedDelay = 3000)
    @SchedulerLock(
            name = "OutboxEventPublisher_publishEvents",
            lockAtLeastFor = "PT2S",
            lockAtMostFor = "PT30S"
    )
    @Transactional
    public void publishEvents() {
        List<OutboxEvent> events = outboxEventRepository.findByProcessedFalse();
        List<OutboxEvent> eventsToUpdate = new ArrayList<>();

        for (OutboxEvent event : events) {
            try {
                OrderStatusUpdateMessage message
                        = objectMapper.readValue(event.getPayload(), OrderStatusUpdateMessage.class);
                boolean sent = streamBridge.send("orderStatus-out-0", message);
                if (sent) {
                    event.setProcessed(true);
                } else {
                    throw new RuntimeException("streamBridge returned false");
                }
            } catch (JsonProcessingException e) {
                logger.error("Serialization error for event {}: {}", event.getId(), e.getMessage());

                DeadLetterEvent dead = DeadLetterEvent.builder()
                        .originalEventId(event.getId())
                        .payload(event.getPayload())
                        .errorDescription("JsonProcessingException: " + e.getMessage())
                        .createdAt(LocalDateTime.now())
                        .build();
                deadLetterRepository.save(dead);

                event.setProcessed(true);
            } catch (Exception e) {
                event.setRetryCount(event.getRetryCount() + 1);
                logger.warn("Retrying event {} (attempt {}/{}), error: {}",
                        event.getId(), event.getRetryCount(), MAX_RETRY_COUNT, e.getMessage());
                if (event.getRetryCount() >= MAX_RETRY_COUNT) {
                    logger.error("Event {} exceeded retry limit, marking as processed", event.getId());
                    event.setProcessed(true);
                }
            }
            eventsToUpdate.add(event);
        }
        if (!eventsToUpdate.isEmpty()) {
            outboxEventRepository.saveAll(eventsToUpdate); // batch update
        }
    }
}
