package com.pimaua.coreService.repository.restaurantServiceRepository;

import com.pimaua.coreService.entity.restaurantServiceEntity.Menu;
import com.pimaua.coreService.entity.restaurantServiceEntity.Restaurant;
import com.pimaua.coreService.test.utils.BaseRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
}
