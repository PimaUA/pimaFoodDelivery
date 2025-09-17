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
import com.pimaua.core.service.order.testdata.OrderTestData;
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
        order = OrderTestData.mockOrder();
        orderResponseDto = OrderTestData.mockOrderResponseDto();
        orderItemRequestDto = OrderTestData.mockOrderItemRequestDto();
        orderItem = OrderTestData.mockOrderItem();
        orderCreateDto = OrderTestData.mockOrderCreateDto();
        orderUpdateDto = OrderTestData.mockOrderUpdateDto();
    }

    @Test
    void create_Success() {
        // Given: a valid restaurant exists and order items can be built
        when(restaurantRepository.existsById(200)).thenReturn(true);
        when(orderItemService.buildOrderItemForOrder(orderItemRequestDto)).thenReturn(orderItem);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderResponseDto);

        // When: creating a new order
        OrderResponseDto result = orderService.create(orderCreateDto, 100);

        // Then: verify order is saved and mapped
        assertNotNull(result);
        verify(orderRepository).save(any(Order.class));
        verify(orderMapper).toDto(order);
    }

    @Test
    void create_RestaurantNotFound() {
        // Given: restaurant does not exist
        when(restaurantRepository.existsById(200)).thenReturn(false);

        // When & Then: creating an order throws RestaurantNotFoundException
        assertThrows(RestaurantNotFoundException.class,
                () -> orderService.create(orderCreateDto, 100));
    }

    @Test
    void findAll_Success() {
        // Given: existing orders in repository
        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(orderMapper.toListDto(anyList())).thenReturn(List.of(orderResponseDto));

        // When: fetching all orders
        List<OrderResponseDto> result = orderService.findAll();

        // Then: verify the returned list and mappin
        assertEquals(1, result.size());
        verify(orderMapper).toListDto(anyList());
    }

    @Test
    void findById_Success() {
        // Given: an existing order with ID 1
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderResponseDto);

        // When: fetching the order by ID
        OrderResponseDto result = orderService.findById(1);

        // Then: verify returned DTO
        assertNotNull(result);
    }

    @Test
    void findById_NotFound() {
        // Given: no order exists with ID 1
        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then: fetching throws OrderNotFoundException
        assertThrows(OrderNotFoundException.class, () -> orderService.findById(1));
    }

    @Test
    void updateOrderLocations_Success() {
        // Given: an existing order and update DTO
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderResponseDto);

        // When: updating order pickup/dropoff locations
        OrderResponseDto result = orderService.updateOrderLocations(1, orderUpdateDto);

        // Then: verify the update, save, and mapping
        assertNotNull(result);
        verify(orderMapper).updateEntity(order, orderUpdateDto);
        verify(orderRepository).save(order);
    }

    @Test
    void updateOrderLocations_NotPending() {
        // Given: order status is not PENDING
        order.setOrderStatus(OrderStatus.DELIVERED);
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        // When & Then: updating locations throws NotUpdatedOrderStatusException
        assertThrows(NotUpdatedOrderStatusException.class,
                () -> orderService.updateOrderLocations(1, orderUpdateDto));
    }

    @Test
    void updateOrderStatus_Success() {
        // Given: an existing order and a valid status transition
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderMapper.toDto(order)).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            return OrderResponseDto.builder()
                    .id(o.getId())
                    .orderStatus(o.getOrderStatus())
                    .build();
        });

        // When: updating order status to CONFIRMED
        OrderResponseDto result = orderService.updateOrderStatus(1, OrderStatus.CONFIRMED);

        // Then: verify order status updated and mapped correctly
        assertEquals(OrderStatus.CONFIRMED, order.getOrderStatus());
        assertEquals(OrderStatus.CONFIRMED, result.getOrderStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void updateOrderStatus_InvalidTransition() {
        // Given: current order status is DELIVERED
        order.setOrderStatus(OrderStatus.DELIVERED);

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        // When & Then: invalid transition throws exception
        InvalidOrderStatusTransitionException exception = assertThrows(
                InvalidOrderStatusTransitionException.class,
                () -> orderService.updateOrderStatus(1, OrderStatus.CONFIRMED)
        );

        // Then: verify exception message
        assertEquals("Cannot transition from DELIVERED to CONFIRMED", exception.getMessage());
    }

    @SuppressWarnings("unchecked")
    @Test
    void findOrdersByUserId_Success() {
        // Given: a page of orders for the user
        Page<Order> page = new PageImpl<>(List.of(order));
        Page<OrderResponseDto> responsePage = new PageImpl<>(List.of(orderResponseDto));
        when(orderRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);
        when(orderMapper.toPageDto(page)).thenReturn(responsePage);

        // When: fetching orders by user ID with pagination
        Page<OrderResponseDto> result = orderService.findOrdersByUserId(
                100, OrderStatus.PENDING, null, null, Pageable.unpaged()
        );

        // Then: verify results and interactions
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(orderResponseDto, result.getContent().get(0));

        verify(orderRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(orderMapper).toPageDto(page);
    }

    @Test
    void recalculateTotalPrice_WhenTotalChanged_ShouldSave() {
        // Given: order with one item and an incorrect total price
        order.addOrderItem(orderItem);
        order.setTotalPrice(BigDecimal.ZERO); // incorrect
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderResponseDto);

        // When: recalculating total price
        OrderResponseDto result = orderService.recalculateTotalPrice(1);

        // Then: total price updated and order saved
        assertNotNull(result);
        assertEquals(order.calculateTotalPrice(), order.getTotalPrice()); // total updated
        verify(orderRepository).save(order); // save must be called
        verify(orderMapper).toDto(order);
    }

    @Test
    void recalculateTotalPrice_WhenTotalUnchanged_ShouldNotSave() {
        // Given: order with correct total price
        order.addOrderItem(orderItem);
        order.setTotalPrice(order.calculateTotalPrice()); // already correct

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderResponseDto);

        // When: recalculating total price
        OrderResponseDto result = orderService.recalculateTotalPrice(1);

        // Then: total price unchanged, order not saved
        assertNotNull(result);
        verify(orderRepository, never()).save(order); // save should NOT be called
        verify(orderMapper).toDto(order);
    }

    @Test
    void deleteOrder_Success() {
        // Given: existing order
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        // When: deleting the order
        orderService.deleteOrder(1);

        // Then: verify order deleted
        verify(orderRepository).delete(order);
    }

    @Test
    void deleteOrder_NotFound() {
        // Given: no order exists with ID 1
        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then: deleting throws OrderNotFoundException
        assertThrows(OrderNotFoundException.class, () -> orderService.deleteOrder(1));
    }

    @Test
    void deleteOrder_NotPending() {
        // Given: order status is not PENDING
        order.setOrderStatus(OrderStatus.DELIVERED);
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        // When & Then: deleting throws OrderDeletionNotAllowedException
        assertThrows(OrderDeletionNotAllowedException.class,
                () -> orderService.deleteOrder(1));
    }
}
