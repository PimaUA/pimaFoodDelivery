package com.pimaua.core.repository.restaurant;

import com.pimaua.core.entity.restaurant.Restaurant;
import com.pimaua.core.test.utils.BaseRepositoryTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("integration")
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
