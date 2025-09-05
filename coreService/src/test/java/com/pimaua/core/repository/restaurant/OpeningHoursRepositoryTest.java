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
        // Create restaurant
        Restaurant restaurant = Restaurant.builder()
                .name("Some Restaurant")
                .address("Some Address")
                .build();
        restaurant = restaurantRepository.save(restaurant);
        // Create OpeningHours
        OpeningHours openingHours = OpeningHours.builder()
                .dayOfWeek(DayOfWeek.FRIDAY)
                .opensAt(LocalTime.of(9, 0))
                .closesAt(LocalTime.of(23, 0))
                .restaurant(restaurant)
                .build();
        openingHoursRepository.save(openingHours);
        // Positive case
        Optional<OpeningHours> foundOpeningHours = openingHoursRepository.findByDayOfWeek(DayOfWeek.FRIDAY);
        assertTrue(foundOpeningHours.isPresent());
        assertEquals(LocalTime.of(9, 0), foundOpeningHours.get().getOpensAt());
        // Negative case
        Optional<OpeningHours> notFound = openingHoursRepository.findByDayOfWeek(DayOfWeek.SUNDAY);
        assertFalse(notFound.isPresent());
    }

    @Test
    void testFindByRestaurantId() {
        // Create restaurant
        Restaurant restaurant = Restaurant.builder()
                .name("Test Restaurant")
                .address("Test Address")
                .build();
        restaurant = restaurantRepository.save(restaurant);
        // Create OpeningHours
        OpeningHours openingHours = OpeningHours.builder()
                .restaurant(restaurant)
                .dayOfWeek(DayOfWeek.MONDAY)
                .opensAt(LocalTime.of(9, 0))
                .closesAt(LocalTime.of(18, 0))
                .build();
        openingHoursRepository.save(openingHours);
        // Positive case
        List<OpeningHours> result = openingHoursRepository.findByRestaurantId(restaurant.getId());
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(DayOfWeek.MONDAY, result.get(0).getDayOfWeek());
        // Negative case (non-existing restaurantId)
        List<OpeningHours> emptyResult = openingHoursRepository.findByRestaurantId(9999);
        assertTrue(emptyResult.isEmpty());
    }
}
