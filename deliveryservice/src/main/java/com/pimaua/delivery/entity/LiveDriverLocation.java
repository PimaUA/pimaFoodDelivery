package com.pimaua.delivery.entity;

import jakarta.persistence.*;
import lombok.*;

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
    private Double latitude;
    private Double longitude;
}
