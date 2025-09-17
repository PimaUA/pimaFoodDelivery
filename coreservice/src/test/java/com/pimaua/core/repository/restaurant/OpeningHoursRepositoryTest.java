package com.pimaua.core.repository.restaurant;

import com.pimaua.core.entity.enums.DayOfWeek;
import com.pimaua.core.entity.restaurant.OpeningHours;
import com.pimaua.core.entity.restaurant.Restaurant;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
@Tag("integration")
public class OpeningHoursRepositoryTest{
    @Autowired
    OpeningHoursRepository openingHoursRepository;
    @Autowired
    RestaurantRepository restaurantRepository;

    @Container
    @ServiceConnection
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

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
