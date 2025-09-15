package com.pimaua.payment.entity;

import com.pimaua.payment.utils.enums.AggregateType;
import com.pimaua.payment.utils.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "outbox_events")
public class OutboxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name="aggregate_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AggregateType aggregateType;
    @Column(name = "aggregate_id", nullable = false)
    private String aggregateId;
    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @Column(columnDefinition = "TEXT")
    private String payload;
    @Builder.Default
    private boolean processed = false;
    @Column(name = "retry_count", nullable = false)
    @Builder.Default
    private Integer retryCount=0;
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
