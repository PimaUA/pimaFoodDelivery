package com.pimaua.core.service.restaurant.testdata;

import com.pimaua.core.dto.restaurant.RestaurantRequestDto;
import com.pimaua.core.dto.restaurant.RestaurantResponseDto;
import com.pimaua.core.entity.enums.DayOfWeek;
import com.pimaua.core.entity.restaurant.Menu;
import com.pimaua.core.entity.restaurant.OpeningHours;
import com.pimaua.core.entity.restaurant.Restaurant;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class RestaurantTestData {

    public static List<Menu> mockMenus() {
        return List.of(
                Menu.builder().id(1).name("Lunch Menu").isActive(true).build(),
                Menu.builder().id(2).name("Dinner Menu").isActive(false).build()
        );
    }

    public static List<OpeningHours> mockOpeningHours() {
        return List.of(
                OpeningHours.builder()
                        .id(1)
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .opensAt(LocalTime.of(9, 0))
                        .closesAt(LocalTime.of(21, 0))
                        .is24Hours(false)
                        .build()
        );
    }

    public static Restaurant mockRestaurant() {
        return Restaurant.builder()
                .id(1)
                .name("Some Restaurant")
                .description("Asian Food")
                .address("Some Address")
                .isActive(true)
                .menus(mockMenus())
                .openingHours(mockOpeningHours())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static RestaurantRequestDto mockRestaurantRequestDto() {
        return RestaurantRequestDto.builder()
                .name("Some Restaurant")
                .description("Asian Food")
                .address("Some Address")
                .isActive(true)
                .build();
    }

    public static RestaurantResponseDto mockRestaurantResponseDto() {
        return RestaurantResponseDto.builder()
                .id(1)
                .name("Some Restaurant")
                .description("Asian Food")
                .address("Some Address")
                .isActive(true)
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
