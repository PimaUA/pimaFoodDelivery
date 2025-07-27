package com.pimaua.coreService.repository.restaurantServiceRepository;

import com.pimaua.coreService.entity.restaurantServiceEntity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
}
