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
import com.pimaua.core.repository.restaurant.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private OrderItemService orderItemService;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private OrderResponseDto orderResponseDto;
    private OrderCreateDto orderCreateDto;
    private OrderItemRequestDto orderItemRequestDto;
    private OrderItem orderItem;
    private OrderUpdateDto orderUpdateDto;

    @BeforeEach
    void setUp() {
        order = Order.builder()
                .id(1)
                .userId(100)
                .restaurantId(200)
                .orderStatus(OrderStatus.PENDING)
                .totalPrice(new BigDecimal("25.00"))
                .createdAt(LocalDateTime.now())
                .build();

        orderResponseDto = OrderResponseDto.builder()
                .id(1)
                .userId(100)
                .restaurantId(200)
                .orderStatus(OrderStatus.PENDING)
                .totalPrice(new BigDecimal("25.00"))
                .build();

        orderItemRequestDto = OrderItemRequestDto.builder()
                .menuItemId(1)
                .quantity(2)
                .build();

        orderItem = OrderItem.builder()
                .id(1)
                .menuItemId(1)
                .quantity(2)
                .unitPrice(new BigDecimal("12.50"))
                .totalPrice(new BigDecimal("25.00"))
                .build();

        orderCreateDto = OrderCreateDto.builder()
                .restaurantId(200)
                .pickupAddress("Pickup")
                .dropOffAddress("DropOff")
                .orderItems(List.of(orderItemRequestDto))
                .build();

        orderUpdateDto = OrderUpdateDto.builder()
                .pickupAddress("New Address")
                .dropOffAddress("New Drop")
                .build();
    }

    @Test
    void create_Success() {
        when(restaurantRepository.existsById(200)).thenReturn(true);
        when(orderItemService.buildOrderItemForOrder(orderItemRequestDto)).thenReturn(orderItem);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderResponseDto);

        OrderResponseDto result = orderService.create(orderCreateDto, 100);

        assertNotNull(result);
        verify(orderRepository).save(any(Order.class));
        verify(orderMapper).toDto(order);
    }

    @Test
    void create_RestaurantNotFound() {
        when(restaurantRepository.existsById(200)).thenReturn(false);
        assertThrows(RestaurantNotFoundException.class,
                () -> orderService.create(orderCreateDto, 100));
    }

    @Test
    void findAll_Success() {
        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(orderMapper.toListDto(anyList())).thenReturn(List.of(orderResponseDto));

        List<OrderResponseDto> result = orderService.findAll();

        assertEquals(1, result.size());
        verify(orderMapper).toListDto(anyList());
    }

    @Test
    void findById_Success() {
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderResponseDto);

        OrderResponseDto result = orderService.findById(1);

        assertNotNull(result);
    }

    @Test
    void findById_NotFound() {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderService.findById(1));
    }

    @Test
    void updateOrderLocations_Success() {
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderResponseDto);

        OrderResponseDto result = orderService.updateOrderLocations(1, orderUpdateDto);

        assertNotNull(result);
        verify(orderMapper).updateEntity(order, orderUpdateDto);
        verify(orderRepository).save(order);
    }

    @Test
    void updateOrderLocations_NotPending() {
        order.setOrderStatus(OrderStatus.DELIVERED);
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        assertThrows(NotUpdatedOrderStatusException.class,
                () -> orderService.updateOrderLocations(1, orderUpdateDto));
    }

    @Test
    void updateOrderStatus_Success() {
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderMapper.toDto(order)).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            return OrderResponseDto.builder()
                    .id(o.getId())
                    .orderStatus(o.getOrderStatus())
                    .build();
        });

        OrderResponseDto result = orderService.updateOrderStatus(1, OrderStatus.CONFIRMED);

        assertEquals(OrderStatus.CONFIRMED, order.getOrderStatus());
        assertEquals(OrderStatus.CONFIRMED, result.getOrderStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void updateOrderStatus_InvalidTransition() {
        // Set the current status to DELIVERED
        order.setOrderStatus(OrderStatus.DELIVERED);

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        // Try to transition DELIVERED -> CONFIRMED, which is invalid
        InvalidOrderStatusTransitionException exception = assertThrows(
                InvalidOrderStatusTransitionException.class,
                () -> orderService.updateOrderStatus(1, OrderStatus.CONFIRMED)
        );

        assertEquals("Cannot transition from DELIVERED to CONFIRMED", exception.getMessage());
    }

    @SuppressWarnings("unchecked")
    @Test
    void findOrdersByUserId_Success() {
        Page<Order> page = new PageImpl<>(List.of(order));
        Page<OrderResponseDto> responsePage = new PageImpl<>(List.of(orderResponseDto));

        when(orderRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);
        when(orderMapper.toPageDto(page)).thenReturn(responsePage);

        Page<OrderResponseDto> result = orderService.findOrdersByUserId(
                100, OrderStatus.PENDING, null, null, Pageable.unpaged()
        );

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(orderResponseDto, result.getContent().get(0));

        verify(orderRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(orderMapper).toPageDto(page);
    }

    @Test
    void recalculateTotalPrice_WhenTotalChanged_ShouldSave() {
        // where: order with one item, totalPrice set differently
        order.addOrderItem(orderItem);
        order.setTotalPrice(BigDecimal.ZERO); // incorrect

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderResponseDto);

        // when
        OrderResponseDto result = orderService.recalculateTotalPrice(1);

        // then
        assertNotNull(result);
        assertEquals(order.calculateTotalPrice(), order.getTotalPrice()); // total updated
        verify(orderRepository).save(order); // save must be called
        verify(orderMapper).toDto(order);
    }

    @Test
    void recalculateTotalPrice_WhenTotalUnchanged_ShouldNotSave() {
        // where: order with one item, totalPrice already correct
        order.addOrderItem(orderItem);
        order.setTotalPrice(order.calculateTotalPrice()); // already correct

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderResponseDto);

        // when
        OrderResponseDto result = orderService.recalculateTotalPrice(1);

        // then
        assertNotNull(result);
        verify(orderRepository, never()).save(order); // save should NOT be called
        verify(orderMapper).toDto(order);
    }

    @Test
    void deleteOrder_Success() {
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        orderService.deleteOrder(1);

        verify(orderRepository).delete(order);
    }

    @Test
    void deleteOrder_NotFound() {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(1));
    }

    @Test
    void deleteOrder_NotPending() {
        order.setOrderStatus(OrderStatus.DELIVERED);
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        assertThrows(OrderDeletionNotAllowedException.class,
                () -> orderService.deleteOrder(1));
    }
}
