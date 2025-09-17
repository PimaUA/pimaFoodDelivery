package com.pimaua.core.service.restaurant.testdata;

import com.pimaua.core.dto.restaurant.MenuItemRequestDto;
import com.pimaua.core.dto.restaurant.MenuRequestDto;
import com.pimaua.core.dto.restaurant.MenuResponseDto;
import com.pimaua.core.entity.restaurant.Menu;
import com.pimaua.core.entity.restaurant.MenuItem;
import com.pimaua.core.entity.restaurant.Restaurant;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class MenuTestData {

    public static Restaurant mockRestaurant() {
        return Restaurant.builder()
                .name("Some Restaurant")
                .address("Some Address")
                .build();
    }

    public static MenuItem mockMenuItem() {
        return MenuItem.builder()
                .id(1)
                .name("Pizza")
                .description("Cheese Pizza")
                .price(BigDecimal.valueOf(9.99))
                .build();
    }

    public static MenuItemRequestDto mockMenuItemRequestDto() {
        return MenuItemRequestDto.builder()
                .name("Pizza")
                .description("Cheese Pizza")
                .price(BigDecimal.valueOf(9.99))
                .build();
    }

    public static Menu mockMenu() {
        return Menu.builder()
                .id(1)
                .name("Some Restaurant")
                .isActive(true)
                .restaurant(mockRestaurant())
                .menuItems(List.of(mockMenuItem()))
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static MenuRequestDto mockMenuRequestDto() {
        return MenuRequestDto.builder()
                .name("Some Restaurant")
                .isActive(true)
                .menuItems(List.of(mockMenuItemRequestDto()))
                .restaurantId(1)
                .build();
    }

    public static MenuResponseDto mockMenuResponseDto() {
        return MenuResponseDto.builder()
                .id(1)
                .name("Some Restaurant")
                .isActive(true)
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
