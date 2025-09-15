package com.pimaua.payment.repository;

import com.pimaua.payment.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Integer> {

    List<OutboxEvent> findByProcessedFalse();
}
