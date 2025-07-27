package com.pimaua.coreService.entity.orderServiceEntity;

import com.pimaua.coreService.entity.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer userId;
    private Integer restaurantId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;
    @Column(name = "total_price", nullable = false)
    private Double totalPrice;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "pickup_address", nullable = false)
    private String pickupAddress;
    @Column(name = "pickup_latitude", nullable = false)
    private Double pickupLatitude;
    @Column(name = "pickup_longitude", nullable = false)
    private Double pickupLongitude;
    @Column(name = "dropoff_address", nullable = false)
    private String dropOffAddress;
    @Column(name = "dropoff_latitude", nullable = false)
    private Double dropOffLatitude;
    @Column(name = "dropoff_longitude", nullable = false)
    private Double dropOffLongitude;
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
}
