package com.pimaua.core.repository.order;

import com.pimaua.core.entity.enums.OrderStatus;
import com.pimaua.core.entity.order.Order;
import com.pimaua.core.entity.restaurant.Restaurant;
import com.pimaua.core.entity.customer.Customer;
import com.pimaua.core.repository.restaurant.RestaurantRepository;
import com.pimaua.core.repository.customer.CustomerRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
@Tag("integration")
public class OrderRepositoryTest{
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    CustomerRepository customerRepository;

    @Container
    @ServiceConnection
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Test
    void testSaveAndFindOrderByStatus() {
        // Step 1: Create and save a Restaurant
        Restaurant restaurant = Restaurant.builder()
                .name("Some Restaurant")
                .address("Some Address")
                .build();
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        //Step 2: Create and save a User
        Customer customer = new Customer();
        customer.setUserId(2);
        customer.setName("John");
        customer.setPhoneNumber("123456");
        Customer savedCustomer = customerRepository.save(customer);

        // Step 3: Create and save an Order
        Order order = Order.builder()
                .userId(savedCustomer.getUserId())
                .restaurantId(savedRestaurant.getId())
                .orderStatus(OrderStatus.CONFIRMED)
                .totalPrice(BigDecimal.valueOf(25.0))
                .createdAt(LocalDateTime.of(2025, 7, 30, 12, 0))
                .pickupAddress("Some street")
                .pickupLatitude(BigDecimal.valueOf(40.1234))
                .pickupLongitude(BigDecimal.valueOf(40.1234))
                .dropOffAddress("Another street")
                .dropOffLatitude(BigDecimal.valueOf(50.1234))
                .dropOffLongitude(BigDecimal.valueOf(50.1234))
                .build();
        orderRepository.save(order);

        // Step 4: Find and assert the saved Order
        Optional<Order> foundOrder = orderRepository.findByOrderStatus(OrderStatus.CONFIRMED);
        assertTrue(foundOrder.isPresent());
        assertEquals(OrderStatus.CONFIRMED, foundOrder.get().getOrderStatus());
    }
}
