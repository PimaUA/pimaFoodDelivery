package com.pimaua.payment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "stripe_customer")
public class StripeCustomer {
    @Id
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    @Column(name = "stripe_customer_id", nullable = false)
    private String stripeCustomerId;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
