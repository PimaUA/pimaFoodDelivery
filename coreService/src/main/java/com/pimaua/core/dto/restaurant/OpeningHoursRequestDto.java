package com.pimaua.core.dto.restaurant;

import com.pimaua.core.entity.enums.DayOfWeek;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OpeningHoursRequestDto {
    @NotNull
    private DayOfWeek dayOfWeek;
    private LocalTime opensAt;
    private LocalTime closesAt;
    private Boolean is24Hours;
}
