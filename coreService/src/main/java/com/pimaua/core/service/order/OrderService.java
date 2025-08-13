package com.pimaua.core.service.order;

import com.pimaua.core.dto.order.*;
import com.pimaua.core.entity.enums.OrderStatus;
import com.pimaua.core.entity.order.Order;
import com.pimaua.core.entity.order.OrderItem;
import com.pimaua.core.exception.custom.notfound.OrderNotFoundException;
import com.pimaua.core.exception.custom.notfound.RestaurantNotFoundException;
import com.pimaua.core.mapper.order.OrderMapper;
import com.pimaua.core.repository.order.OrderRepository;
import com.pimaua.core.repository.restaurant.RestaurantRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final RestaurantRepository restaurantRepository;
    private final OrderItemService orderItemService;

    public OrderResponseDto create(OrderCreateDto orderCreateDto, Integer userId) {
        // 1. Validate restaurant exists
        if (!restaurantRepository.existsById(orderCreateDto.getRestaurantId())) {
            throw new RestaurantNotFoundException("Restaurant not found with ID " +
                    orderCreateDto.getRestaurantId());
        }

        // 2. Create order entity (without items first)
        Order order = Order.builder()
                .userId(userId)
                .restaurantId(orderCreateDto.getRestaurantId())
                .pickupAddress(orderCreateDto.getPickupAddress())
                .pickupLatitude(orderCreateDto.getPickupLatitude())
                .pickupLongitude(orderCreateDto.getPickupLongitude())
                .dropOffAddress(orderCreateDto.getDropOffAddress())
                .dropOffLatitude(orderCreateDto.getDropOffLatitude())
                .dropOffLongitude(orderCreateDto.getDropOffLongitude())
                .orderStatus(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        // 3. Create and add order items
        List<OrderItemRequestDto> requestItems = orderCreateDto.getOrderItems();
        if (requestItems != null && !requestItems.isEmpty()) {
            for (OrderItemRequestDto itemRequestDto : requestItems) {
                OrderItem orderItem = orderItemService.buildOrderItem(itemRequestDto);
                order.addOrderItem(orderItem);
            }
        }
        order.setTotalPrice(order.calculateTotalPrice());

        // 4. Save and return
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> findAll() {
        List<Order> orders = orderRepository.findAll();
        return orderMapper.toListDto(orders);
    }

    @Transactional(readOnly = true)
    public OrderResponseDto findById(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID " + id));
        return orderMapper.toDto(order);
    }

    public OrderResponseDto update(Integer id, OrderUpdateDto orderUpdateDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID " + id));
        orderMapper.updateEntity(order, orderUpdateDto);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    public OrderResponseDto recalculateOrderTotal(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with ID " + orderId));
        order.setTotalPrice(order.calculateTotalPrice());
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    public void delete(Integer id) {
        int deletedCount = orderRepository.deleteOrderById(id);
        if (deletedCount == 0) {
            throw new OrderNotFoundException("Order not found with ID " + id);
        }
    }
}
