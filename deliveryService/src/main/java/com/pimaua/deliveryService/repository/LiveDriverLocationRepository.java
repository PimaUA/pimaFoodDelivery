package com.pimaua.deliveryService.repository;

import com.pimaua.deliveryService.entity.Driver;
import com.pimaua.deliveryService.entity.LiveDriverLocation;
import com.pimaua.deliveryService.entity.LiveDriverLocationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LiveDriverLocationRepository extends JpaRepository<LiveDriverLocation,LiveDriverLocationId> {
    Optional<LiveDriverLocation>findByDriver(Driver driver);
}
