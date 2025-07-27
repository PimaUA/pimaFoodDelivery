package com.pimaua.coreService.entity.restaurantServiceEntity;

import com.pimaua.coreService.entity.enums.DayOfWeek;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
    @Column(name = "is24Hours")
    private Boolean is24Hours;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
