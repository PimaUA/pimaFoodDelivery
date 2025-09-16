package com.pimaua.delivery.repository;

import com.pimaua.delivery.entity.Driver;
import com.pimaua.delivery.entity.LiveDriverLocation;
import com.pimaua.delivery.entity.LiveDriverLocationId;
import com.pimaua.delivery.entity.enums.DriverStatus;
import com.pimaua.delivery.entity.enums.VehicleType;
import com.pimaua.delivery.test.utils.BaseRepositoryTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("integration")
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
