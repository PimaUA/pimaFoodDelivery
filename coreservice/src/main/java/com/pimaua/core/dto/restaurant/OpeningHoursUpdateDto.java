package com.pimaua.core.dto.restaurant;

import com.pimaua.core.entity.enums.DayOfWeek;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OpeningHoursUpdateDto {
    @NotNull
    private DayOfWeek dayOfWeek;
    private LocalTime opensAt;
    private LocalTime closesAt;
    @NotNull
    private Boolean is24Hours;
}
