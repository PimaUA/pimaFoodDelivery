package com.pimaua.core.repository.restaurant;

import com.pimaua.core.entity.restaurant.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    Optional<Menu> findByName(String name);

    Page<Menu> findByRestaurantIdAndIsActiveTrue(Integer id, Pageable pageable);
}
