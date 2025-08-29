package com.pimaua.payment.mapper;

import com.pimaua.payment.utils.enums.PaymentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StripePaymentStatusMapper {
    private static final Logger logger = LoggerFactory.getLogger(StripePaymentStatusMapper.class);

    public PaymentStatus map(String stripeStatus) {
        try {
            return PaymentStatus.valueOf(stripeStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warn("Unmapped Stripe status received: {}", stripeStatus);
            return PaymentStatus.UNKNOWN; // fallback
        }
    }
}
