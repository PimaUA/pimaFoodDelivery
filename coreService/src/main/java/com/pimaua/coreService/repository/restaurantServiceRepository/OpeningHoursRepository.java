package com.pimaua.coreService.repository.restaurantServiceRepository;

import com.pimaua.coreService.entity.restaurantServiceEntity.OpeningHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpeningHoursRepository extends JpaRepository<OpeningHours, Integer> {
}
