package com.pimaua.deliveryService.repository;

import com.pimaua.deliveryService.entity.Driver;
import com.pimaua.deliveryService.entity.enums.DriverStatus;
import com.pimaua.deliveryService.entity.enums.VehicleType;
import com.pimaua.deliveryService.test.utils.BaseRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
