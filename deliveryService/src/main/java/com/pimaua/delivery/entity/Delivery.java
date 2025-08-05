package com.pimaua.delivery.entity;

import com.pimaua.delivery.entity.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "delivery")
@Builder
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "order_id", nullable = false)
    private Integer orderId;
    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", nullable = false)
    private DeliveryStatus deliveryStatus;
    @Column(name = "pickup_time")
    private LocalDateTime pickupTime;
    @Column(name = "dropoff_time")
    private LocalDateTime dropOffTime;
    @Column(name = "estimated_time")
    private LocalDateTime estimatedTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id",nullable = false)
    private Driver driver;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
