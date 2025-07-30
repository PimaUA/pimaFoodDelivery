package com.pimaua.coreService.repository.restaurantServiceRepository;

import com.pimaua.coreService.entity.enums.DayOfWeek;
import com.pimaua.coreService.entity.restaurantServiceEntity.OpeningHours;
import com.pimaua.coreService.entity.restaurantServiceEntity.Restaurant;
import com.pimaua.coreService.test.utils.BaseRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
}
