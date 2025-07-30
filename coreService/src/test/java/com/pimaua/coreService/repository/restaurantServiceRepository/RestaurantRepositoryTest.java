package com.pimaua.coreService.repository.restaurantServiceRepository;

import com.pimaua.coreService.entity.restaurantServiceEntity.Restaurant;
import com.pimaua.coreService.test.utils.BaseRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RestaurantRepositoryTest extends BaseRepositoryTest {
    @Autowired
    RestaurantRepository restaurantRepository;

    @Test
    void testSaveAndFindRestaurant() {
        // Step 1: Create and save a Restaurant
        Restaurant restaurant = Restaurant.builder()
                .name("Some Restaurant")
                .address("Some Address")
                .build();
        restaurantRepository.save(restaurant);

        // Step 2: Find and assert the saved Restaurant
        Optional<Restaurant> foundRestaurant = restaurantRepository.findByName("Some Restaurant");
        assertTrue(foundRestaurant.isPresent());
        assertEquals("Some Restaurant", foundRestaurant.get().getName());
    }
}
