package com.pimaua.payment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "stripe_processed_events")
public class ProcessedEvent {
    @Id
    private String id; // Stripe event ID
}