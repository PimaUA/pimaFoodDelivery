package com.pimaua.delivery.repository;

import com.pimaua.delivery.entity.Driver;
import com.pimaua.delivery.entity.LiveDriverLocation;
import com.pimaua.delivery.entity.LiveDriverLocationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LiveDriverLocationRepository extends JpaRepository<LiveDriverLocation,LiveDriverLocationId> {
    Optional<LiveDriverLocation>findByDriver(Driver driver);
}
