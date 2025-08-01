package com.pimaua.deliveryService.repository;

import com.pimaua.deliveryService.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {
    Optional<Delivery>findByOrderId(Integer orderId);
}
