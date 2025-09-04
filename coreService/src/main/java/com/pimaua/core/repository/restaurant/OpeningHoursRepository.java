package com.pimaua.core.repository.restaurant;

import com.pimaua.core.entity.enums.DayOfWeek;
import com.pimaua.core.entity.restaurant.OpeningHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OpeningHoursRepository extends JpaRepository<OpeningHours, Integer> {
    Optional<OpeningHours>findByDayOfWeek(DayOfWeek dayOfWeek);

    List<OpeningHours>findByRestaurantId(Integer restaurantId);

    List<OpeningHours> findByRestaurantIdOrderByDayOfWeekAsc(Integer restaurantId);
}
