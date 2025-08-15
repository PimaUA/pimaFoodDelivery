package com.pimaua.core.repository.restaurant;

import com.pimaua.core.entity.restaurant.Menu;
import com.pimaua.core.entity.restaurant.MenuItem;
import com.pimaua.core.entity.restaurant.Restaurant;
import com.pimaua.core.test.utils.BaseRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MenuItemRepositoryTest extends BaseRepositoryTest {
    @Autowired
    MenuItemRepository menuItemRepository;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    RestaurantRepository restaurantRepository;

    @Test
    void testSaveAndFindMenuItem(){
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
        menu = menuRepository.save(menu);

        // Step 3: Create and save a MenuItem linked to the Menu
        MenuItem menuItem = MenuItem.builder()
                .name("Apple Juice")
                .price(BigDecimal.valueOf(20.0))
                .menu(menu)
                .build();
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);

        // Step 4: Find and assert the saved MenuItem
        Optional<MenuItem> foundMenuItem = menuItemRepository.findByName("Apple Juice");
        assertTrue(foundMenuItem.isPresent());
        assertEquals("Apple Juice", foundMenuItem.get().getName());
    }
}
