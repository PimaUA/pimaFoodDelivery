package com.pimaua.delivery.repository;

import com.pimaua.delivery.entity.Driver;
import com.pimaua.delivery.entity.LiveDriverLocation;
import com.pimaua.delivery.entity.LiveDriverLocationId;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
@Tag("integration")
public class LiveDriverLocationRepositoryTest{
    @Autowired
    LiveDriverLocationRepository liveDriverLocationRepository;
    @Autowired
    DriversRepository driversRepository;

    @Container
    @ServiceConnection
    static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Test
    void saveAndFindLiveDeliveryLocation() {
        // Given: create and save a Driver
        Driver driver = Driver.builder()
                .userId(1)
                .name("John")
                .driverStatus(DriverStatus.AVAILABLE)
                .vehicleType(VehicleType.CAR)
                .driverLocation("Kyiv")
                .updatedAt(LocalDateTime.now())
                .build();
        Driver savedDriver = driversRepository.save(driver);

        // Given: create and save a LiveDriverLocation for the Driver
        LocalDateTime timestamp = LocalDateTime.now();
        LiveDriverLocationId locationId = new LiveDriverLocationId(savedDriver.getId(), timestamp);

        LiveDriverLocation liveDriverLocation = LiveDriverLocation.builder()
                .liveDriverLocationId(locationId)
                .driver(savedDriver)
                .latitude(50.4501)
                .longitude(30.5234)
                .build();
        liveDriverLocationRepository.save(liveDriverLocation);

        // When: fetch LiveDriverLocation by Driver
        Optional<LiveDriverLocation> found = liveDriverLocationRepository.findByDriver(savedDriver);

        // Then: verify it exists and has the expected values
        assertTrue(found.isPresent());
        assertEquals(savedDriver.getName(), found.get().getDriver().getName());
        assertEquals(50.4501, found.get().getLatitude(), 0.000001);
        assertEquals(30.5234, found.get().getLongitude(), 0.000001);
    }
}
