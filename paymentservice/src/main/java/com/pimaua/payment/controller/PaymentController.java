package com.pimaua.payment.controller;

import com.pimaua.payment.dto.PaymentCreateDto;
import com.pimaua.payment.service.PaymentService;
import com.stripe.model.PaymentIntent;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/api/payment", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/create/{orderId}")
    public Map<String, String> createPayment(@Valid @RequestBody PaymentCreateDto paymentCreateDto,
                                             @PathVariable Integer orderId) {
        PaymentIntent intent = paymentService.createPaymentIntent(paymentCreateDto, orderId);
        return Map.of("client_secret", intent.getClientSecret());
    }
}
