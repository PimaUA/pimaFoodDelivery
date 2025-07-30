package com.pimaua.coreService.repository.restaurantServiceRepository;

import com.pimaua.coreService.entity.enums.DayOfWeek;
import com.pimaua.coreService.entity.restaurantServiceEntity.OpeningHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OpeningHoursRepository extends JpaRepository<OpeningHours, Integer> {
    Optional<OpeningHours>findByDayOfWeek(DayOfWeek dayOfWeek);
}
