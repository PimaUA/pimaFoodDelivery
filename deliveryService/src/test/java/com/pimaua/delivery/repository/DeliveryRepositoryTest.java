package com.pimaua.delivery.repository;

import com.pimaua.delivery.entity.Delivery;
import com.pimaua.delivery.entity.Driver;
import com.pimaua.delivery.entity.enums.DeliveryStatus;
import com.pimaua.delivery.entity.enums.DriverStatus;
import com.pimaua.delivery.entity.enums.VehicleType;
import com.pimaua.delivery.test.utils.BaseRepositoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeliveryRepositoryTest extends BaseRepositoryTest {
    @Autowired
    DeliveryRepository deliveryRepository;
    @Autowired
    DriversRepository driversRepository;

    @Test
    void testSaveAndFindDelivery() {
        Driver driver = Driver.builder()
                .userId(1)
                .name("John")
                .driverStatus(DriverStatus.AVAILABLE)
                .vehicleType(VehicleType.CAR)
                .driverLocation("Kyiv")
                .updatedAt(LocalDateTime.now())
                .build();

        Driver savedDriver=driversRepository.save(driver);

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

        Optional<Delivery> foundDelivery = deliveryRepository.findByOrderId(1);
        assertTrue(foundDelivery.isPresent());
        assertEquals(1, foundDelivery.get().getOrderId());
    }
}
