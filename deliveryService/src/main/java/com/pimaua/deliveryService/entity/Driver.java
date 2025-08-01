package com.pimaua.deliveryService.entity;

import com.pimaua.deliveryService.entity.enums.DriverStatus;
import com.pimaua.deliveryService.entity.enums.VehicleType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "drivers")
@Builder
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_id", nullable = false)
    private Integer userId;
    @Column(nullable = false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(name = "driver_status", nullable = false)
    private DriverStatus driverStatus;
    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false)
    private VehicleType vehicleType;
    @Column(name = "driver_location")
    private String driverLocation;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @OneToMany(mappedBy = "driver",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Delivery> deliveryList=new ArrayList<>();
    @OneToMany(mappedBy = "driver",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LiveDriverLocation> liveDriverLocations=new ArrayList<>();
}
