package com.pimaua.payment.service.impl;

import com.pimaua.payment.exception.custom.MissingMetaDataException;
import com.pimaua.payment.service.PaymentService;
import com.pimaua.payment.service.events.ProcessedEventService;
import com.pimaua.payment.service.StripeWebhookService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import static com.pimaua.payment.utils.enums.PaymentStatus.FAILED;
import static com.pimaua.payment.utils.enums.PaymentStatus.SUCCEEDED;

@Service
@RequiredArgsConstructor
public class StripeWebhookServiceImpl implements StripeWebhookService {
    @Value("${stripe.webhook-secret}")
    private String endpointSecret;
    private final ProcessedEventService processedEventService;
    private final PaymentService paymentService;
    private final StreamBridge streamBridge;
    private static final Logger logger = LoggerFactory.getLogger(StripeWebhookServiceImpl.class);

    public ResponseEntity<String> handleStripeEvent(String payload,
                                                    String sigHeader) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            logger.error("Webhook signature verification failed", e);
            return ResponseEntity.badRequest().build();
        }
        //Idempotency check with DB
        if (processedEventService.isProcessed(event.getId())) {
            logger.info("Skipping already processed event: {}", event.getId());
            return ResponseEntity.ok().build();
        }
        //Deserialize nested object
        StripeObject stripeObject = null;
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            logger.warn("Deserialization failed, possibly due to API version mismatch.");
        }
        //Handle event safely
        handleStripeEvent(event, stripeObject);
        //Mark as processed
        processedEventService.markProcessed(event.getId());
        return ResponseEntity.ok().build();
    }

    private void handleStripeEvent(Event event, StripeObject stripeObject) {
        switch (event.getType()) {
            case "payment_intent.succeeded":
                if (stripeObject instanceof PaymentIntent paymentIntent) {
                    handlePaymentIntentSucceeded(paymentIntent);
                }
                break;
            case "payment_intent.payment_failed":
                if (stripeObject instanceof PaymentIntent paymentIntent) {
                    handlePaymentIntentFailed(paymentIntent, event);
                }
                break;
            default:
                logger.info("Unhandled event type: {}", event.getType());
                break;
        }
    }

    private void handlePaymentIntentSucceeded(PaymentIntent paymentIntent) {
        logger.info("Payment for {} succeeded.", paymentIntent.getAmount());
        String orderIdStr = paymentIntent.getMetadata().get("order_id");
        if (orderIdStr == null) {
            throw new MissingMetaDataException("Missing orderId in PaymentIntent metadata");
        }
        Integer orderId = Integer.valueOf(orderIdStr);

        paymentService.updatePaymentStatusAndCreateOutbox(paymentIntent.getId(), SUCCEEDED, orderId);
    }

    private void handlePaymentIntentFailed(PaymentIntent paymentIntent, Event event) {
        logger.info("Payment failed");
        String orderIdStr = paymentIntent.getMetadata().get("order_id");
        if (orderIdStr == null) {
            throw new MissingMetaDataException("Missing orderId in PaymentIntent metadata");
        }
        Integer orderId = Integer.valueOf(orderIdStr);

        paymentService.updatePaymentStatusAndCreateOutbox(paymentIntent.getId(), FAILED, orderId);
    }
}
