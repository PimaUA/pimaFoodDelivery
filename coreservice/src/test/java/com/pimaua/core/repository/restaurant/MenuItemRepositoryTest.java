package com.pimaua.core.repository.restaurant;

import com.pimaua.core.entity.restaurant.Menu;
import com.pimaua.core.entity.restaurant.MenuItem;
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

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
@Tag("integration")
public class MenuItemRepositoryTest{
    @Autowired
    MenuItemRepository menuItemRepository;
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
    void testSaveAndFindMenuItem(){
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
        menu = menuRepository.save(menu);

        // and: a menu item linked to the menu
        MenuItem menuItem = MenuItem.builder()
                .id(1)
                .name("Apple Juice")
                .price(BigDecimal.valueOf(20.0))
                .menu(menu)
                .build();
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);

        // when: searching for the menu item by name
        Optional<MenuItem> foundMenuItem = menuItemRepository.findByName("Apple Juice");

        // then: the menu item is found with the expected name
        assertTrue(foundMenuItem.isPresent());
        assertEquals("Apple Juice", foundMenuItem.get().getName());
    }
}
