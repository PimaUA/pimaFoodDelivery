package com.pimaua.coreService.repository.restaurantServiceRepository;

import com.pimaua.coreService.entity.restaurantServiceEntity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
}
