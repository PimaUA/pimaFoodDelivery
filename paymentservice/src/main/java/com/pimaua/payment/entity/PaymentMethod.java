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
@Table(name = "payment_method")
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    @Column(name = "stripe_payment_method_id", nullable = false)
    private String stripePaymentMethodId;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String brand;
    @Column(name = "exp_month", nullable = false)
    private Integer expMonth;
    @Column(name = "exp_year",nullable = false)
    private Integer expYear;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
