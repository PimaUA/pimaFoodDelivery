package com.pimaua.deliveryService.repository;

import com.pimaua.deliveryService.entity.Driver;
import com.pimaua.deliveryService.entity.LiveDriverLocation;
import com.pimaua.deliveryService.entity.LiveDriverLocationId;
import com.pimaua.deliveryService.entity.enums.DriverStatus;
import com.pimaua.deliveryService.entity.enums.VehicleType;
import com.pimaua.deliveryService.test.utils.BaseRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LiveDriverLocationRepositoryTest extends BaseRepositoryTest {
    @Autowired
    LiveDriverLocationRepository liveDriverLocationRepository;
    @Autowired
    DriversRepository driversRepository;

    @Test
    void saveAndFindLiveDeliveryLocation() {
        Driver driver = Driver.builder()
                .userId(1)
                .name("John")
                .driverStatus(DriverStatus.AVAILABLE)
                .vehicleType(VehicleType.CAR)
                .driverLocation("Kyiv")
                .updatedAt(LocalDateTime.now())
                .build();

        Driver savedDriver = driversRepository.save(driver);

        LocalDateTime timestamp = LocalDateTime.now();
        LiveDriverLocationId locationId = new LiveDriverLocationId(savedDriver.getId(), timestamp);

        LiveDriverLocation liveDriverLocation = LiveDriverLocation.builder()
                .liveDriverLocationId(locationId)
                .driver(savedDriver)
                .latitude(BigDecimal.valueOf(50.4501))
                .longitude(BigDecimal.valueOf(30.5234))
                .build();

        LiveDriverLocation savedLiveDriverLocation = liveDriverLocationRepository.save(liveDriverLocation);

        Optional<LiveDriverLocation> foundLiveDriverLocation = liveDriverLocationRepository.findByDriver(savedDriver);
        assertTrue(foundLiveDriverLocation.isPresent());
        assertEquals(savedDriver.getName(), foundLiveDriverLocation.get().getDriver().getName());
        assertEquals(BigDecimal.valueOf(50.4501).stripTrailingZeros(), foundLiveDriverLocation.get().getLatitude().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(30.5234).stripTrailingZeros(), foundLiveDriverLocation.get().getLongitude().stripTrailingZeros());
    }
}
