package com.pimaua.payment.controller;

import com.pimaua.payment.service.StripeWebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stripe/webhook")
public class StripeWebhookController {
    private final StripeWebhookService stripeWebhookService;

    @PostMapping
    public ResponseEntity<String> handleStripeEvent(@RequestBody String payload,
                                                    @RequestHeader("Stripe-Signature") String sigHeader) {
        return stripeWebhookService.handleStripeEvent(payload, sigHeader);
    }
}