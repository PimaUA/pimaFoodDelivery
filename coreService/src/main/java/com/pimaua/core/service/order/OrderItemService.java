package com.pimaua.core.service.order;

import com.pimaua.core.dto.order.OrderItemRequestDto;
import com.pimaua.core.dto.order.OrderItemResponseDto;
import com.pimaua.core.entity.enums.OrderStatus;
import com.pimaua.core.entity.order.Order;
import com.pimaua.core.entity.order.OrderItem;
import com.pimaua.core.entity.restaurant.MenuItem;
import com.pimaua.core.exception.custom.notfound.OrderItemNotFoundException;
import com.pimaua.core.exception.custom.notfound.OrderNotFoundException;
import com.pimaua.core.mapper.order.OrderItemMapper;
import com.pimaua.core.repository.order.OrderItemRepository;
import com.pimaua.core.repository.order.OrderRepository;
import com.pimaua.core.service.restaurant.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(OrderItemService.class);

    public OrderItemResponseDto addItemToExistingOrder(Integer orderId, OrderItemRequestDto orderItemRequestDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    logger.error("Order not found with id={}", orderId);
                    return new OrderNotFoundException("Order not found with ID " + orderId);
                });
        // Check if order is still modifiable
        if (!order.getOrderStatus().equals(OrderStatus.PENDING)) {
            throw new IllegalStateException("Cannot add items to order with status: " + order.getOrderStatus());
        }
        OrderItem orderItem = buildOrderItemForOrder(orderItemRequestDto);
        order.addOrderItem(orderItem);
        order.setTotalPrice(order.calculateTotalPrice());
        orderRepository.save(order);
        return orderItemMapper.toDto(orderItem);
    }

    public List<OrderItemResponseDto> findAll() {
        return orderItemMapper.toListDto(orderItemRepository.findAll());
    }

    public OrderItemResponseDto findById(Integer id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("OrderItem not found with id={}", id);
                    return new OrderItemNotFoundException("OrderItem not found with ID " + id);
                });
        return orderItemMapper.toDto(orderItem);
    }

    public List<OrderItemResponseDto> findByOrderId(Integer orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        if (orderItems.isEmpty()) {
            logger.error("No OrderItems found for orderId={}", orderId);
            throw new OrderItemNotFoundException("No OrderItems found for order ID " + orderId);
        }
        return orderItemMapper.toListDto(orderItems);
    }

    public OrderItemResponseDto update(Integer id, OrderItemRequestDto orderItemRequestDto) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("OrderItem not found with id={}", id);
                    return new OrderItemNotFoundException("OrderItem not found with ID " + id);
                });
        Order order = orderItem.getOrder();
        if (!order.getOrderStatus().equals(OrderStatus.PENDING)) {
            throw new IllegalStateException("Cannot update items to order with status: " + order.getOrderStatus());
        }
        applyUpdatedMenuItemAndPrices(orderItemRequestDto, orderItem);
        order.setTotalPrice(order.calculateTotalPrice());
        orderRepository.save(order);
        return orderItemMapper.toDto(orderItem);
    }

    public void delete(Integer id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("OrderItem not found with id={}", id);
                    return new OrderItemNotFoundException("OrderItem not found with ID " + id);
                });
        Order order = orderItem.getOrder();
        if (!order.getOrderStatus().equals(OrderStatus.PENDING)) {
            throw new IllegalStateException("Cannot delete items from order with status: " + order.getOrderStatus());
        }
        order.getOrderItems().remove(orderItem);
        order.setTotalPrice(order.calculateTotalPrice());
        orderRepository.save(order);
    }

    public OrderItem buildOrderItemForOrder(OrderItemRequestDto orderItemRequestDto) {
        PriceInfo priceInfo = validateQuantityAndCalculateTotal(orderItemRequestDto);
        return orderItemMapper.toEntity(orderItemRequestDto, priceInfo.menuItem,
                priceInfo.unitPrice, priceInfo.totalPrice);
    }

    private void applyUpdatedMenuItemAndPrices(OrderItemRequestDto orderItemRequestDto, OrderItem orderItem) {
        PriceInfo priceInfo = validateQuantityAndCalculateTotal(orderItemRequestDto);
        orderItemMapper.updateEntity(orderItem, orderItemRequestDto,
                priceInfo.menuItem, priceInfo.unitPrice, priceInfo.totalPrice);
    }

    private record PriceInfo(MenuItem menuItem, BigDecimal unitPrice, BigDecimal totalPrice) {
    }

    private PriceInfo validateQuantityAndCalculateTotal(OrderItemRequestDto orderItemRequestDto) {
        if (orderItemRequestDto.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        MenuItem menuItem = menuItemService.getMenuItemById(orderItemRequestDto.getMenuItemId());
        BigDecimal unitPrice = menuItem.getPrice();
        BigDecimal totalPrice = calculateTotalPrice(unitPrice, orderItemRequestDto.getQuantity());
        return new PriceInfo(menuItem, unitPrice, totalPrice);
    }

    private BigDecimal calculateTotalPrice(BigDecimal unitPrice, int quantity) {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
