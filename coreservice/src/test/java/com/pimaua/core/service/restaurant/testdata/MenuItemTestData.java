package com.pimaua.core.service.restaurant.testdata;

import com.pimaua.core.dto.restaurant.MenuItemRequestDto;
import com.pimaua.core.dto.restaurant.MenuItemResponseDto;
import com.pimaua.core.entity.restaurant.Menu;
import com.pimaua.core.entity.restaurant.MenuItem;
import com.pimaua.core.entity.restaurant.Restaurant;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MenuItemTestData {

    public static MenuItem mockMenuItem() {
        Restaurant restaurant = Restaurant.builder()
                .name("Some Restaurant")
                .address("Some Address")
                .build();

        Menu menu = Menu.builder()
                .name("Lunch Menu")
                .restaurant(restaurant)
                .build();

        return MenuItem.builder()
                .id(1)
                .name("Water")
                .description("Some water")
                .price(BigDecimal.valueOf(10.0))
                .isAvailable(true)
                .menu(menu)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static MenuItemRequestDto mockMenuItemRequestDto() {
        return MenuItemRequestDto.builder()
                .name("Water")
                .description("Some water")
                .price(BigDecimal.valueOf(10.0))
                .isAvailable(true)
                .build();
    }

    public static MenuItemResponseDto mockMenuItemResponseDto() {
        return MenuItemResponseDto.builder()
                .id(1)
                .name("Water")
                .description("Some water")
                .price(BigDecimal.valueOf(10.0))
                .isAvailable(true)
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
