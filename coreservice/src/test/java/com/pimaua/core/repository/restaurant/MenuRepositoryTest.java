package com.pimaua.core.repository.restaurant;

import com.pimaua.core.entity.restaurant.Menu;
import com.pimaua.core.entity.restaurant.Restaurant;
import com.pimaua.core.test.utils.BaseRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MenuRepositoryTest extends BaseRepositoryTest {
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    RestaurantRepository restaurantRepository;

    @Test
    void testSaveAndFindMenu() {
        // Step 1: Create and save a Restaurant
        Restaurant restaurant = Restaurant.builder()
                .name("Some Restaurant")
                .address("Some Address")
                .build();
        restaurant = restaurantRepository.save(restaurant);

        // Step 2: Create and save a Menu linked to the Restaurant
        Menu menu = Menu.builder()
                .name("Lunch Menu")
                .restaurant(restaurant)
                .build();
        menuRepository.save(menu);

        // Step 3: Find and assert the saved Menu
        Optional<Menu> foundMenu = menuRepository.findByName("Lunch Menu");
        assertTrue(foundMenu.isPresent());
        assertEquals("Lunch Menu", foundMenu.get().getName());
    }

    @Test
    void testFindByRestaurantIdAndIsActiveTrue() {
        // Step 1: Create and save a Restaurant
        Restaurant restaurant = Restaurant.builder()
                .name("Test Restaurant")
                .address("Test Address")
                .build();
        restaurant = restaurantRepository.save(restaurant);

        // Step 2: Create Menus (one active, one inactive)
        Menu activeMenu = Menu.builder()
                .name("Active Menu")
                .restaurant(restaurant)
                .isActive(true)
                .build();
        Menu inactiveMenu = Menu.builder()
                .name("Inactive Menu")
                .restaurant(restaurant)
                .isActive(false)
                .build();
        menuRepository.saveAll(List.of(activeMenu, inactiveMenu));

        // Step 3: Fetch active menus with pageable
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Page<Menu> result = menuRepository.findByRestaurantIdAndIsActiveTrue(restaurant.getId(), pageable);

        // Step 4: Assertions
        assertEquals(1, result.getTotalElements());
        assertEquals("Active Menu", result.getContent().get(0).getName());
        assertTrue(result.getContent().get(0).getIsActive());
    }
}
