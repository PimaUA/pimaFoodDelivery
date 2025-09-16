package com.pimaua.delivery.repository;

import com.pimaua.delivery.entity.Driver;
import com.pimaua.delivery.entity.enums.DriverStatus;
import com.pimaua.delivery.entity.enums.VehicleType;
import com.pimaua.delivery.test.utils.BaseRepositoryTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("integration")
public class DriversRepositoryTest extends BaseRepositoryTest {
    @Autowired
    DriversRepository driversRepository;

    @Test
    void saveAndFindDriver(){
        Driver driver = Driver.builder()
                .userId(1)
                .name("John")
                .driverStatus(DriverStatus.AVAILABLE)
                .vehicleType(VehicleType.CAR)
                .driverLocation("Kyiv")
                .updatedAt(LocalDateTime.now())
                .build();

        driversRepository.save(driver);

        Optional<Driver>foundDriver=driversRepository.findByName("John");
        assertTrue(foundDriver.isPresent());
        assertEquals("John",foundDriver.get().getName());
    }
}
