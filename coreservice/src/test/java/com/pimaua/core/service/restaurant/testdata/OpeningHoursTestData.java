package com.pimaua.core.service.restaurant.testdata;

import com.pimaua.core.dto.restaurant.OpeningHoursRequestDto;
import com.pimaua.core.dto.restaurant.OpeningHoursResponseDto;
import com.pimaua.core.dto.restaurant.OpeningHoursUpdateDto;
import com.pimaua.core.entity.enums.DayOfWeek;
import com.pimaua.core.entity.restaurant.OpeningHours;
import com.pimaua.core.entity.restaurant.Restaurant;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class OpeningHoursTestData {

    public static Restaurant mockRestaurant() {
        return Restaurant.builder()
                .id(1)
                .name("Some Restaurant")
                .address("Some Address")
                .build();
    }

    public static OpeningHours mockOpeningHours(Restaurant restaurant) {
        return OpeningHours.builder()
                .id(1)
                .dayOfWeek(DayOfWeek.MONDAY)
                .opensAt(LocalTime.of(9, 0))
                .closesAt(LocalTime.of(22, 0))
                .is24Hours(false)
                .updatedAt(LocalDateTime.now())
                .restaurant(restaurant)
                .build();
    }

    public static OpeningHoursRequestDto mockOpeningHoursRequestDto(Restaurant restaurant) {
        return OpeningHoursRequestDto.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .opensAt(LocalTime.of(9, 0))
                .closesAt(LocalTime.of(22, 0))
                .is24Hours(false)
                .restaurantId(restaurant.getId())
                .build();
    }

    public static OpeningHoursUpdateDto mockOpeningHoursUpdateDto() {
        return OpeningHoursUpdateDto.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .opensAt(LocalTime.of(9, 0))
                .closesAt(LocalTime.of(22, 0))
                .is24Hours(false)
                .build();
    }

    public static OpeningHoursResponseDto mockOpeningHoursResponseDto() {
        return OpeningHoursResponseDto.builder()
                .id(1)
                .dayOfWeek(DayOfWeek.MONDAY)
                .opensAt(LocalTime.of(9, 0))
                .closesAt(LocalTime.of(22, 0))
                .is24Hours(false)
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
