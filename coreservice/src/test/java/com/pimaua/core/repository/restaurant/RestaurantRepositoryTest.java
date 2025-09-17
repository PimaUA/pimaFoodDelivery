package com.pimaua.core.repository.restaurant;

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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
@Tag("integration")
public class RestaurantRepositoryTest{
    @Autowired
    RestaurantRepository restaurantRepository;

    @Container
    @ServiceConnection
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Test
    void testSaveAndFindRestaurant() {
        // given: a restaurant to be saved
        Restaurant restaurant = Restaurant.builder()
                .name("Some Restaurant")
                .address("Some Address")
                .build();
        restaurantRepository.save(restaurant);

        // when: searching for the restaurant by name
        Optional<Restaurant> foundRestaurant = restaurantRepository.findByName("Some Restaurant");

        // then: the restaurant is found and has the expected name
        assertTrue(foundRestaurant.isPresent());
        assertEquals("Some Restaurant", foundRestaurant.get().getName());
    }
}
