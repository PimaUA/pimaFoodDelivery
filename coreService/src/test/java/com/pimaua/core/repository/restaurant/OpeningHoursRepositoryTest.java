package com.pimaua.core.repository.restaurant;

import com.pimaua.core.entity.enums.DayOfWeek;
import com.pimaua.core.entity.restaurant.OpeningHours;
import com.pimaua.core.entity.restaurant.Restaurant;
import com.pimaua.core.test.utils.BaseRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class OpeningHoursRepositoryTest extends BaseRepositoryTest {
    @Autowired
    OpeningHoursRepository openingHoursRepository;
    @Autowired
    RestaurantRepository restaurantRepository;

    @Test
    void testSaveAndFindOpeningHours() {
        // Step 1: Create and save a Restaurant
        Restaurant restaurant = Restaurant.builder()
                .name("Some Restaurant")
                .address("Some Address")
                .build();
        restaurant = restaurantRepository.save(restaurant);

        // Step 2: Create and save a OpeningHours
        OpeningHours openingHours = OpeningHours.builder()
                .dayOfWeek(DayOfWeek.FRIDAY)
                .opensAt(LocalTime.of(9, 0))
                .closesAt(LocalTime.of(23, 0))
                .restaurant(restaurant)
                .build();
        openingHoursRepository.save(openingHours);

        // Step 3: Find and assert the saved OpeningHours
        Optional<OpeningHours> foundOpeningHours = openingHoursRepository.findByDayOfWeek(DayOfWeek.FRIDAY);
        assertTrue(foundOpeningHours.isPresent());
        assertEquals(LocalTime.of(9, 0), foundOpeningHours.get().getOpensAt());
    }

    @Test
    void testFindByRestaurantId(){
        // Step 1: Create and save a Restaurant
        Restaurant restaurant = Restaurant.builder()
                .name("Some Restaurant")
                .address("Some Address")
                .build();
        restaurant = restaurantRepository.save(restaurant);

        // Step 2: Create and save OpeningHours linked to the restaurant
        OpeningHours openingHours = OpeningHours.builder()
                .restaurant(restaurant)
                .dayOfWeek(DayOfWeek.MONDAY)
                .opensAt(LocalTime.of(9, 0))
                .closesAt(LocalTime.of(18, 0))
                .build();
        openingHoursRepository.save(openingHours);

        // Step 3: Call repository method
        List<OpeningHours> result = openingHoursRepository.findByRestaurantId(restaurant.getId());

        // Step 4: Verify results
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(DayOfWeek.MONDAY, result.get(0).getDayOfWeek());
        assertEquals(LocalTime.of(9, 0), result.get(0).getOpensAt());
        assertEquals(restaurant.getId(), result.get(0).getRestaurant().getId());
    }
}
