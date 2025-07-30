package com.pimaua.coreService.entity.restaurantServiceEntity;

import com.pimaua.coreService.entity.enums.DayOfWeek;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "opening_hours")
public class OpeningHours {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;
    @Column(name = "opens_at", nullable = false)
    private LocalTime opensAt;
    @Column(name = "closes_at", nullable = false)
    private LocalTime closesAt;
    @Column(name = "is_24_hours")
    private Boolean is24Hours;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
