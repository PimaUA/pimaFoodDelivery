package com.pimaua.payment.service;

import org.springframework.http.ResponseEntity;

public interface StripeWebhookService {
    ResponseEntity<String> handleStripeEvent(String payload,
                                             String sigHeader);
}
