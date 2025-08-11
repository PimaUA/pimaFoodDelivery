package com.pimaua.core.entity.restaurant;

import com.pimaua.core.entity.enums.DayOfWeek;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;
    @Column(name = "opens_at")
    private LocalTime opensAt;
    @Column(name = "closes_at")
    private LocalTime closesAt;
    @Column(name = "is_24_hours",columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Builder.Default
    private Boolean is24Hours=false;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
