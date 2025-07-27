package com.pimaua.coreService.repository.restaurantServiceRepository;

import com.pimaua.coreService.entity.restaurantServiceEntity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
}
