package com.pimaua.delivery.repository;

import com.pimaua.delivery.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriversRepository extends JpaRepository<Driver,Integer> {
    Optional<Driver>findByName(String name);
}
