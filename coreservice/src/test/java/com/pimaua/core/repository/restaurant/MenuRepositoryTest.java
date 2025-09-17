package com.pimaua.core.repository.restaurant;

import com.pimaua.core.entity.restaurant.Menu;
import com.pimaua.core.entity.restaurant.Restaurant;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
@Tag("integration")
public class MenuRepositoryTest{
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    RestaurantRepository restaurantRepository;

    @Container
    @ServiceConnection
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Test
    void testSaveAndFindMenu() {
        // given: a restaurant
        Restaurant restaurant = Restaurant.builder()
                .name("Some Restaurant")
                .address("Some Address")
                .build();
        restaurant = restaurantRepository.save(restaurant);

        // and: a menu linked to the restaurant
        Menu menu = Menu.builder()
                .name("Lunch Menu")
                .restaurant(restaurant)
                .build();
        menuRepository.save(menu);

        // when: searching for the menu by name
        Optional<Menu> foundMenu = menuRepository.findByName("Lunch Menu");

        // then: the menu is found with the expected name
        assertTrue(foundMenu.isPresent());
        assertEquals("Lunch Menu", foundMenu.get().getName());
    }

    @Test
    void testFindByRestaurantIdAndIsActiveTrue() {
        // given: a restaurant
        Restaurant restaurant = Restaurant.builder()
                .name("Test Restaurant")
                .address("Test Address")
                .build();
        restaurant = restaurantRepository.save(restaurant);

        // and: two menus (one active, one inactive)
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

        // when: fetching active menus with pagination
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Page<Menu> result = menuRepository.findByRestaurantIdAndIsActiveTrue(restaurant.getId(), pageable);

        // then: only the active menu is returned
        assertEquals(1, result.getTotalElements());
        assertEquals("Active Menu", result.getContent().get(0).getName());
        assertTrue(result.getContent().get(0).getIsActive());
    }
}
