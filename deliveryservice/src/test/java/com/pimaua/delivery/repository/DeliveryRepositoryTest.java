package com.pimaua.delivery.repository;

import com.pimaua.delivery.entity.Delivery;
import com.pimaua.delivery.entity.Driver;
import com.pimaua.delivery.entity.enums.DeliveryStatus;
import com.pimaua.delivery.entity.enums.DriverStatus;
import com.pimaua.delivery.entity.enums.VehicleType;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
@Tag("integration")
public class DeliveryRepositoryTest{
    @Autowired
    DeliveryRepository deliveryRepository;
    @Autowired
    DriversRepository driversRepository;

    @Container
    @ServiceConnection
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Test
    void testSaveAndFindDelivery() {
        // Given: create and save a Driver
        Driver driver = Driver.builder()
                .userId(1)
                .name("John")
                .driverStatus(DriverStatus.AVAILABLE)
                .vehicleType(VehicleType.CAR)
                .driverLocation("Kyiv")
                .updatedAt(LocalDateTime.now())
                .build();

        Driver savedDriver=driversRepository.save(driver);

        // Given: create and save a Delivery linked to the Driver
        Delivery delivery = Delivery.builder()
                .orderId(1)
                .deliveryStatus(DeliveryStatus.DELIVERED)
                .pickupTime(LocalDateTime.now())
                .dropOffTime(LocalDateTime.now())
                .estimatedTime(LocalDateTime.of(2025, 8, 1, 12, 30))
                .driver(savedDriver)
                .updatedAt(LocalDateTime.now())
                .build();
        Delivery savedDelivery = deliveryRepository.save(delivery);

        // When: retrieve the Delivery by orderId
        Optional<Delivery> foundDelivery = deliveryRepository.findByOrderId(1);

        // Then: verify the Delivery was found and matches the saved data
        assertTrue(foundDelivery.isPresent());
        assertEquals(1, foundDelivery.get().getOrderId());
    }
}
