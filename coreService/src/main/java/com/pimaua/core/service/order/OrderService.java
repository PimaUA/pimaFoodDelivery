package com.pimaua.core.service.order;

import com.pimaua.core.dto.order.OrderCreateDto;
import com.pimaua.core.dto.order.OrderResponseDto;
import com.pimaua.core.dto.order.OrderUpdateDto;
import com.pimaua.core.entity.order.Order;
import com.pimaua.core.exception.custom.OrderNotFoundException;
import com.pimaua.core.mapper.order.OrderMapper;
import com.pimaua.core.repository.order.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderResponseDto create(OrderCreateDto orderCreateDto) {
        Order order = orderMapper.toEntity(orderCreateDto);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    public List<OrderResponseDto> findAll() {
        List<Order> orders = orderRepository.findAll();
        return orderMapper.toListDto(orders);
    }

    public OrderResponseDto findById(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID " + id));
        return orderMapper.toDto(order);
    }

    public OrderResponseDto update(Integer id, OrderUpdateDto orderUpdateDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID " + id));
        orderMapper.updateEntity(order, orderUpdateDto);
        Order savedOrder=orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    public void delete(Integer id) {
        if (!orderRepository.existsById(id)) {
            throw new OrderNotFoundException("Order not found with ID " + id);
        }
        orderRepository.deleteById(id);
    }
}
