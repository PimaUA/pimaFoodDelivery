package com.pimaua.core.repository.order;

import com.pimaua.core.entity.customer.Customer;
import com.pimaua.core.entity.enums.OrderStatus;
import com.pimaua.core.entity.order.Order;
import com.pimaua.core.entity.order.OrderItem;
import com.pimaua.core.entity.restaurant.Menu;
import com.pimaua.core.entity.restaurant.MenuItem;
import com.pimaua.core.entity.restaurant.Restaurant;
import com.pimaua.core.repository.customer.CustomerRepository;
import com.pimaua.core.repository.restaurant.MenuItemRepository;
import com.pimaua.core.repository.restaurant.MenuRepository;
import com.pimaua.core.repository.restaurant.RestaurantRepository;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
@Tag("integration")
public class OrderItemRepositoryTest{
    @Autowired
    OrderItemRepository orderItemRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    MenuRepository menuRepository;
    @Autowired
    MenuItemRepository menuItemRepository;

    @Container
    @ServiceConnection
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Test
    void testSaveAndFindOrderItemByName() {
        // Setup: Restaurant, Customer, Order
        Restaurant restaurant = restaurantRepository.save(Restaurant.builder()
                .name("Some Restaurant")
                .address("Some Address")
                .build());

        Customer customer = customerRepository.save(Customer.builder()
                .userId(2)
                .name("John")
                .phoneNumber("1234567")
                .build());

        Order order = orderRepository.save(Order.builder()
                .userId(customer.getUserId())
                .restaurantId(restaurant.getId())
                .orderStatus(OrderStatus.CONFIRMED)
                .totalPrice(BigDecimal.valueOf(25.0))
                .createdAt(LocalDateTime.now())
                .pickupAddress("Some street")
                .pickupLatitude(BigDecimal.valueOf(40.1234))
                .pickupLongitude(BigDecimal.valueOf(40.1234))
                .dropOffAddress("Another street")
                .dropOffLatitude(BigDecimal.valueOf(50.1234))
                .dropOffLongitude(BigDecimal.valueOf(50.1234))
                .build());

        // Setup: Menu & MenuItem
        Menu menu = menuRepository.save(Menu.builder()
                .name("Lunch Menu")
                .restaurant(restaurant)
                .build());

        MenuItem menuItem = menuItemRepository.save(MenuItem.builder()
                .id(1)
                .name("Apple Juice")
                .price(BigDecimal.valueOf(20.0))
                .menu(menu)
                .build());

        // Save OrderItem
        OrderItem orderItem = orderItemRepository.save(OrderItem.builder()
                .name("Apple Juice")
                .menuItemId(menuItem.getId())
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(10.0))
                .totalPrice(BigDecimal.valueOf(20.0))
                .order(order)
                .build());

        // Verify findByName
        Optional<OrderItem> found = orderItemRepository.findByName("Apple Juice");
        assertTrue(found.isPresent());
        assertEquals("Apple Juice", found.get().getName());
        assertEquals(order.getId(), found.get().getOrder().getId());
    }

    @Test
    void testFindByOrderId() {
        // --- Setup Restaurant & Customer ---
        Restaurant restaurant = restaurantRepository.save(Restaurant.builder()
                .name("Test Restaurant")
                .address("Test Address")
                .build());

        Customer customer = customerRepository.save(Customer.builder()
                .userId(3)
                .name("Jane")
                .phoneNumber("7654321")
                .build());

        // --- Setup Order ---
        Order order = orderRepository.save(Order.builder()
                .userId(customer.getUserId())
                .restaurantId(restaurant.getId())
                .orderStatus(OrderStatus.PENDING)
                .totalPrice(BigDecimal.valueOf(45.0))
                .createdAt(LocalDateTime.now())
                .pickupAddress("Pickup street")
                .pickupLatitude(BigDecimal.valueOf(41.1234))
                .pickupLongitude(BigDecimal.valueOf(41.1234))
                .dropOffAddress("Dropoff street")
                .dropOffLatitude(BigDecimal.valueOf(51.1234))
                .dropOffLongitude(BigDecimal.valueOf(51.1234))
                .build());

        // --- Setup Menu & MenuItems ---
        Menu menu = menuRepository.save(Menu.builder()
                .name("Dinner Menu")
                .restaurant(restaurant)
                .build());

        MenuItem menuItem1 = menuItemRepository.save(MenuItem.builder()
                .id(10)
                .name("Burger")
                .price(BigDecimal.valueOf(15.0))
                .menu(menu)
                .build());

        MenuItem menuItem2 = menuItemRepository.save(MenuItem.builder()
                .id(20)
                .name("Fries")
                .price(BigDecimal.valueOf(8.0))
                .menu(menu)
                .build());

        MenuItem menuItem3 = menuItemRepository.save(MenuItem.builder()
                .id(30)
                .name("Cola")
                .price(BigDecimal.valueOf(5.0))
                .menu(menu)
                .build());

        // --- Setup OrderItems ---
        orderItemRepository.save(OrderItem.builder()
                .name("Burger")
                .menuItemId(menuItem1.getId())
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(15.0))
                .totalPrice(BigDecimal.valueOf(30.0))
                .order(order)
                .build());

        orderItemRepository.save(OrderItem.builder()
                .name("Fries")
                .menuItemId(menuItem2.getId())
                .quantity(1)
                .unitPrice(BigDecimal.valueOf(8.0))
                .totalPrice(BigDecimal.valueOf(8.0))
                .order(order)
                .build());

        orderItemRepository.save(OrderItem.builder()
                .name("Cola")
                .menuItemId(menuItem3.getId())
                .quantity(1)
                .unitPrice(BigDecimal.valueOf(5.0))
                .totalPrice(BigDecimal.valueOf(5.0))
                .order(order)
                .build());

        // --- Verify findByOrderId ---
        List<OrderItem> items = orderItemRepository.findByOrderId(order.getId());
        assertEquals(3, items.size(), "There should be 3 order items");

        int totalQuantity = items.stream().mapToInt(OrderItem::getQuantity).sum();
        assertEquals(4, totalQuantity, "Total quantity should be 4");

        BigDecimal totalPrice = items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        assertEquals(0, BigDecimal.valueOf(43.0).compareTo(totalPrice), "Total price should be 43.0");
    }
}
