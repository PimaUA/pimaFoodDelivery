package com.pimaua.payment.service;

import com.pimaua.payment.exception.custom.MissingMetaDataException;
import com.pimaua.payment.service.events.ProcessedEventService;
import com.pimaua.payment.service.impl.StripeWebhookServiceImpl;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;
import java.util.Optional;

import static com.pimaua.payment.utils.enums.PaymentStatus.SUCCEEDED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class StripeWebhookServiceImplTest {

    @Mock
    private ProcessedEventService processedEventService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private StreamBridge streamBridge;

    @InjectMocks
    private StripeWebhookServiceImpl webhookService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(webhookService, "endpointSecret", "test_secret");
    }

    @Test
    void handleStripeEvent_InvalidSignature_ReturnsBadRequest() {
        // given: an invalid Stripe signature header
        String payload = "{}";
        String sigHeader = "invalid";

        // when: webhook service handles the event
        ResponseEntity<String> response = webhookService.handleStripeEvent(payload, sigHeader);

        // then: request is rejected with HTTP 400 Bad Request
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void handleStripeEvent_AlreadyProcessed_ReturnsOk() throws Exception {
        // given: an incoming event with a known ID that was already processed
        Event event = mock(Event.class);
        when(event.getId()).thenReturn("evt_1");
        when(processedEventService.isProcessed("evt_1")).thenReturn(true);

        // and: Stripe Webhook.constructEvent is mocked to return this event
        try (MockedStatic<Webhook> webhookStatic = mockStatic(Webhook.class)) {
            webhookStatic.when(() -> Webhook.constructEvent(anyString(), anyString(), anyString()))
                    .thenReturn(event);

            // when: webhook service handles the event
            ResponseEntity<String> response = webhookService.handleStripeEvent("payload", "sig");

            // then: request succeeds with HTTP 200 OK but payment is not re-processed
            assertEquals(200, response.getStatusCodeValue());
            verify(processedEventService).isProcessed("evt_1");
        }
    }

    @Test
    void handleStripeEvent_SuccessPaymentIntent_CallsUpdatePayment() throws Exception {
        // given: an unprocessed "payment_intent.succeeded" event with metadata
        Event event = mock(Event.class);
        PaymentIntent pi = mock(PaymentIntent.class);

        when(event.getType()).thenReturn("payment_intent.succeeded");
        when(event.getId()).thenReturn("evt_2");

        // and: event contains a deserialized PaymentIntent with metadata and ID
        EventDataObjectDeserializer deserializer = mock(EventDataObjectDeserializer.class);
        when(event.getDataObjectDeserializer()).thenReturn(deserializer);
        when(deserializer.getObject()).thenReturn(Optional.of(pi));

        when(pi.getMetadata()).thenReturn(Map.of("order_id", "123"));
        when(pi.getId()).thenReturn("pi_12345"); // <--- stub ID properly

        when(processedEventService.isProcessed("evt_2")).thenReturn(false);

        // and: Stripe Webhook.constructEvent is mocked to return this event
        try (MockedStatic<Webhook> webhookStatic = mockStatic(Webhook.class)) {
            webhookStatic.when(() -> Webhook.constructEvent(anyString(), anyString(), anyString()))
                    .thenReturn(event);

            // when: webhook service handles the event
            ResponseEntity<String> response = webhookService.handleStripeEvent("payload", "sig");

            // then: request succeeds with HTTP 200 OK, payment is updated, and event is marked processed
            assertEquals(200, response.getStatusCodeValue());
            verify(paymentService).updatePaymentStatusAndCreateOutbox("pi_12345", SUCCEEDED, 123);
            verify(processedEventService).markProcessed("evt_2");
        }
    }

    @Test
    void handleStripeEvent_MissingOrderId_ThrowsException() throws Exception {
        // given: an unprocessed "payment_intent.succeeded" event without order_id metadata
        Event event = mock(Event.class);
        PaymentIntent pi = mock(PaymentIntent.class);
        when(event.getType()).thenReturn("payment_intent.succeeded");
        when(event.getId()).thenReturn("evt_3");
        when(event.getDataObjectDeserializer()).thenReturn(mock(EventDataObjectDeserializer.class));
        when(pi.getMetadata()).thenReturn(Map.of());
        when(event.getDataObjectDeserializer().getObject()).thenReturn(Optional.of(pi));
        when(processedEventService.isProcessed("evt_3")).thenReturn(false);

        // and: Stripe Webhook.constructEvent is mocked to return this event
        try (MockedStatic<Webhook> webhookStatic = mockStatic(Webhook.class)) {
            webhookStatic.when(() -> Webhook.constructEvent(anyString(), anyString(), anyString()))
                    .thenReturn(event);

            // when & then: handling the event throws MissingMetaDataException
            assertThrows(MissingMetaDataException.class,
                    () -> webhookService.handleStripeEvent("payload", "sig"));
        }
    }
}
