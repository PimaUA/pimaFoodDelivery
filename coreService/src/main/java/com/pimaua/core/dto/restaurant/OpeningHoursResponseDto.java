package com.pimaua.core.dto.restaurant;

import com.pimaua.core.entity.enums.DayOfWeek;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OpeningHoursResponseDto {
    private Integer id;
    private DayOfWeek dayOfWeek;
    private LocalTime opensAt;
    private LocalTime closesAt;
    private Boolean is24Hours;
    private LocalDateTime updatedAt;
}
