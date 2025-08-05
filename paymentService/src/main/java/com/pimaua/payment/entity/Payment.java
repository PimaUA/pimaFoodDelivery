package com.pimaua.payment.entity;

import com.pimaua.payment.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "order_id", nullable = false)
    private Integer orderId;
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = false)
    private String currency;
    @Column(name = "status",nullable = false)
    private PaymentStatus paymentStatus;
    @Column(name = "stripe_payment_intent_id",nullable = false)
    private String stripePaymentIntentId;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
