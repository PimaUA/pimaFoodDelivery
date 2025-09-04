package com.pimaua.core.dto.restaurant;

import com.pimaua.core.entity.enums.DayOfWeek;
import com.pimaua.core.validation.ValidOpeningHours;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;

@ValidOpeningHours
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpeningHoursRequestDto {
    @NotNull
    private DayOfWeek dayOfWeek;
    private LocalTime opensAt;
    private LocalTime closesAt;
    @NotNull
    private Boolean is24Hours;
    @NotNull
    private Integer restaurantId;
}
