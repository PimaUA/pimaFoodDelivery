package com.pimaua.core.service.order;

import com.pimaua.core.dto.order.*;
import com.pimaua.core.entity.enums.OrderStatus;
import com.pimaua.core.entity.order.Order;
import com.pimaua.core.entity.order.OrderItem;
import com.pimaua.core.exception.InvalidOrderStatusTransitionException;
import com.pimaua.core.exception.NotUpdatedOrderStatusException;
import com.pimaua.core.exception.OrderDeletionNotAllowedException;
import com.pimaua.core.exception.custom.notfound.OrderNotFoundException;
import com.pimaua.core.exception.custom.notfound.RestaurantNotFoundException;
import com.pimaua.core.mapper.order.OrderMapper;
import com.pimaua.core.repository.order.OrderRepository;
import com.pimaua.core.repository.order.spec.OrderSpecs;
import com.pimaua.core.repository.restaurant.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final RestaurantRepository restaurantRepository;
    private final OrderItemService orderItemService;
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public OrderResponseDto create(OrderCreateDto orderCreateDto, Integer userId) {
        // 1. Validate restaurant exists
        if (!restaurantRepository.existsById(orderCreateDto.getRestaurantId())) {
            logger.error("Restaurant not found with id={}", orderCreateDto.getRestaurantId());
            throw new RestaurantNotFoundException("Restaurant not found with ID " +
                    orderCreateDto.getRestaurantId());
        }
        // 2. Create order
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
        // 3. Attach items before saving
        List<OrderItemRequestDto> requestItems = orderCreateDto.getOrderItems();
        if (requestItems != null && !requestItems.isEmpty()) {
            for (OrderItemRequestDto itemRequestDto : requestItems) {
                OrderItem orderItem = orderItemService.buildOrderItemForOrder(itemRequestDto);
                order.addOrderItem(orderItem);
            }
        }
        // 4. Calculate total price and save only once
        order.setTotalPrice(order.calculateTotalPrice());
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
                .orElseThrow(() -> {
                    logger.error("Order not found with id={}", id);
                    return new OrderNotFoundException("Order not found with ID " + id);
                });
        return orderMapper.toDto(order);
    }

    public OrderResponseDto updateOrderLocations(Integer id, OrderUpdateDto orderUpdateDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Order not found with id={}", id);
                    return new OrderNotFoundException("Order not found with ID " + id);
                });
        if (order.getOrderStatus().equals(OrderStatus.PENDING)) {
            orderMapper.updateEntity(order, orderUpdateDto);
            orderRepository.save(order);
        } else {
            throw new NotUpdatedOrderStatusException("Only orders with PENDING status can be updated");
        }
        return orderMapper.toDto(order);
    }

    public OrderResponseDto updateOrderStatus(Integer orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    logger.error("Order not found with id={}", orderId);
                    return new OrderNotFoundException("Order not found with ID " + orderId);
                });
        OrderStatus currentStatus = order.getOrderStatus();
        if (!currentStatus.canTransitionTo(newStatus)) {
            throw new InvalidOrderStatusTransitionException(String.format
                    ("Cannot transition from %s to %s", currentStatus, newStatus));
        }
        order.setOrderStatus(newStatus);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    public Page<OrderResponseDto> findOrdersByUserId(Integer userId, OrderStatus orderStatus,
                                                     LocalDate from, LocalDate to, Pageable pageable) {

        Specification<Order> spec = buildOrderSpecification(userId, orderStatus, from, to);
        Page<Order> orders = orderRepository.findAll(spec, pageable);
        return orderMapper.toPageDto(orders);
    }

    public OrderResponseDto recalculateTotalPrice(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    logger.error("Order not found with id={}", orderId);
                    return new OrderNotFoundException("Order not found with ID " + orderId);
                });

        BigDecimal newTotal = order.calculateTotalPrice();
        if (order.getTotalPrice() == null || order.getTotalPrice().compareTo(newTotal) != 0) {
            order.setTotalPrice(newTotal);
            orderRepository.save(order);
        }
        return orderMapper.toDto(order);
    }

    public void deleteOrder(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Order not found with id={}", id);
                    return new OrderNotFoundException("Order not found with ID " + id);
                });
        if (!order.getOrderStatus().equals(OrderStatus.PENDING)) {
            throw new OrderDeletionNotAllowedException("Cannot delete order with status: " + order.getOrderStatus());
        }
        orderRepository.delete(order);
    }

    private Specification<Order> buildOrderSpecification(Integer userId,
                                                         OrderStatus orderStatus,
                                                         LocalDate from,
                                                         LocalDate to) {
        //Validation
        if (from != null && to != null) {
            if (from.isAfter(to)) {
                throw new IllegalArgumentException("`from` must be before or equal to `to`");
            }
            if (ChronoUnit.DAYS.between(from, to) > 93) {
                throw new IllegalArgumentException("Date range cannot exceed 93 days");
            }
        }
        //Convert LocalDate → LocalDateTime
        LocalDateTime fromDateTime = from != null ? from.atStartOfDay() : LocalDateTime.MIN;
        LocalDateTime toDateTime = to != null ? to.atTime(LocalTime.MAX) : LocalDateTime.MAX;
        //Build Specification
        return Specification.allOf(
                OrderSpecs.hasUserId(userId),
                OrderSpecs.hasOrderStatus(orderStatus),
                OrderSpecs.createdFromTo(fromDateTime, toDateTime)
        );
    }
}
