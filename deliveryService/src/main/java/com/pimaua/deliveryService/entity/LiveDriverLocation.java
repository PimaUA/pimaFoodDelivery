package com.pimaua.deliveryService.entity;

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
@Table(name = "live_driver_locations")
public class LiveDriverLocation {
    @EmbeddedId
    private LiveDriverLocationId liveDriverLocationId;

    @MapsId("driverId") // Maps embedded driverId to this relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id",nullable = false)
    private Driver driver;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
