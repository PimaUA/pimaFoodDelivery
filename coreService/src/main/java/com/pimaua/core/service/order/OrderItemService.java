package com.pimaua.core.service.order;

import com.pimaua.core.dto.order.OrderItemRequestDto;
import com.pimaua.core.dto.order.OrderItemResponseDto;
import com.pimaua.core.entity.order.Order;
import com.pimaua.core.entity.order.OrderItem;
import com.pimaua.core.entity.restaurant.MenuItem;
import com.pimaua.core.exception.custom.notfound.OrderItemNotFoundException;
import com.pimaua.core.mapper.order.OrderItemMapper;
import com.pimaua.core.repository.order.OrderItemRepository;
import com.pimaua.core.repository.order.OrderRepository;
import com.pimaua.core.service.restaurant.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemMapper orderItemMapper;
    private final MenuItemService menuItemService;

    public OrderItem createOrderItem(OrderItemRequestDto orderItemRequestDto) {
        OrderItem orderItem = prepareOrderItem(orderItemRequestDto);
        return orderItemRepository.save(orderItem);
    }

    public List<OrderItemResponseDto> findAll() {
        return orderItemMapper.toListDto(orderItemRepository.findAll());
    }

    public OrderItemResponseDto findById(Integer id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new OrderItemNotFoundException("OrderItem not found with ID " + id));
        return orderItemMapper.toDto(orderItem);
    }

    public OrderItemResponseDto update(Integer id, OrderItemRequestDto orderItemRequestDto) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new OrderItemNotFoundException("OrderItem not found with ID " + id));
        MenuItem menuItem = menuItemService.getMenuItemById(orderItemRequestDto.getMenuItemId());
        BigDecimal unitPrice = menuItem.getPrice();
        BigDecimal totalPrice = calculateTotalPrice(unitPrice, orderItemRequestDto.getQuantity());
        orderItemMapper.updateEntity(orderItem, orderItemRequestDto, menuItem, unitPrice, totalPrice);
        OrderItem savedOrderItem = orderItemRepository.save(orderItem);

        Order order = savedOrderItem.getOrder();
        order.setTotalPrice(order.calculateTotalPrice());
        orderRepository.save(order);

        return orderItemMapper.toDto(savedOrderItem);
    }

    public void delete(Integer id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new OrderItemNotFoundException("OrderItem not found with ID: " + id));

        Order order = orderItem.getOrder();
        order.getOrderItems().remove(orderItem);
        order.setTotalPrice(order.calculateTotalPrice());

        orderRepository.save(order);
    }

    private BigDecimal calculateTotalPrice(BigDecimal unitPrice, int quantity) {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public OrderItem buildOrderItem(OrderItemRequestDto orderItemRequestDto) {
        return prepareOrderItem(orderItemRequestDto);
    }

    private OrderItem prepareOrderItem(OrderItemRequestDto orderItemRequestDto) {
        MenuItem menuItem = menuItemService.getMenuItemById(orderItemRequestDto.getMenuItemId());
        BigDecimal unitPrice = menuItem.getPrice();
        BigDecimal totalPrice = calculateTotalPrice(unitPrice, orderItemRequestDto.getQuantity());
        return orderItemMapper.toEntity(orderItemRequestDto, menuItem, unitPrice, totalPrice);
    }
}
