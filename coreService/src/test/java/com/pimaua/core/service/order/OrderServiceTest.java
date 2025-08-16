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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
    private OrderCreateDto orderCreateDto;
    private OrderUpdateDto orderUpdateDto;
    private OrderResponseDto orderResponseDto;
    private OrderItemRequestDto orderItemRequestDto;
    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        // Setup test data
        order = Order.builder()
                .id(1)
                .userId(100)
                .restaurantId(200)
                .pickupAddress("123 Pickup St")
                .pickupLatitude(new BigDecimal("40.7128"))
                .pickupLongitude(new BigDecimal("-74.0060"))
                .dropOffAddress("456 Delivery Ave")
                .dropOffLatitude(new BigDecimal("40.7589"))
                .dropOffLongitude(new BigDecimal("-73.9851"))
                .orderStatus(OrderStatus.PENDING)
                .totalPrice(new BigDecimal("25.99"))
                .createdAt(LocalDateTime.now())
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
                .restaurantId(1)
                .pickupAddress("One Street")
                .pickupLatitude(new BigDecimal("40.7128"))
                .pickupLongitude(new BigDecimal("-74.0060"))
                .dropOffAddress("Another Street")
                .dropOffLatitude(new BigDecimal("40.7589"))
                .dropOffLongitude(new BigDecimal("-73.9851"))
                .orderItems(Arrays.asList(orderItemRequestDto))
                .build();

        orderUpdateDto = OrderUpdateDto.builder()
                .pickupAddress("One Street")
                .pickupLatitude(new BigDecimal("40.7128"))
                .pickupLongitude(new BigDecimal("-74.0060"))
                .dropOffAddress("Another Street")
                .dropOffLatitude(new BigDecimal("40.7589"))
                .dropOffLongitude(new BigDecimal("-73.9851"))
                .build();

        orderResponseDto = OrderResponseDto.builder()
                .id(1)
                .userId(2)
                .restaurantId(3)
                .orderStatus(OrderStatus.PENDING)
                .totalPrice(new BigDecimal("23.00"))
                .build();
    }

    @Test
    void create_Success() {
        when(restaurantRepository.existsById(orderCreateDto.getRestaurantId())).thenReturn(true);
        when(orderItemService.buildOrderItem(orderItemRequestDto)).thenReturn(orderItem);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderResponseDto);

        OrderResponseDto result = orderService.create(orderCreateDto, 100);

        assertNotNull(result);
        verify(restaurantRepository).existsById(orderCreateDto.getRestaurantId());
        verify(orderItemService).buildOrderItem(orderItemRequestDto);
        verify(orderRepository).save(any(Order.class));
        verify(orderMapper).toDto(order);
    }

    @Test
    void create_RestaurantNotFound() {
        when(restaurantRepository.existsById(orderCreateDto.getRestaurantId())).thenReturn(false);
        assertThrows(RestaurantNotFoundException.class,
                () -> orderService.create(orderCreateDto, 100));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void findAll_Success() {
        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(orderMapper.toListDto(anyList())).thenReturn(List.of(orderResponseDto));
        List<OrderResponseDto> result = orderService.findAll();
        assertEquals(1, result.size());
        verify(orderRepository).findAll();
        verify(orderMapper).toListDto(anyList());
    }

    @Test
    void findById_Success() {
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderResponseDto);
        OrderResponseDto result = orderService.findById(1);
        assertNotNull(result);
        verify(orderRepository).findById(1);
    }

    @Test
    void findById_OrderNotFound() {
        when(orderRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderService.findById(1));
    }

    @Test
    void update_Success() {
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderResponseDto);
        OrderResponseDto result = orderService.update(1, orderUpdateDto);
        assertNotNull(result);
        verify(orderMapper).updateEntity(order, orderUpdateDto);
        verify(orderRepository).save(order);
    }

    @Test
    void recalculateOrderTotal_Success() {
        order.addOrderItem(orderItem);
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderResponseDto);

        OrderResponseDto result = orderService.recalculateTotalPrice(1);

        assertNotNull(result);
        assertEquals(new BigDecimal("25.00"), order.getTotalPrice());
    }

    @Test
    void delete_ShouldRemove_Success() {
        when(orderRepository.deleteOrderById(1)).thenReturn(1);
        orderService.delete(1);
        verify(orderRepository).deleteOrderById(1);
    }

    @Test
    void delete_OrderNotFound() {
        when(orderRepository.deleteOrderById(1)).thenReturn(0);
        assertThrows(OrderNotFoundException.class, () -> orderService.delete(1));
    }

    @Test
    void calculateTotalPrice_Success() {
        Order order1 = new Order();
        order1.addOrderItem(OrderItem.builder().totalPrice(new BigDecimal("5.00")).build());
        order1.addOrderItem(OrderItem.builder().totalPrice(new BigDecimal("15.00")).build());

        assertEquals(new BigDecimal("20.00"), order1.calculateTotalPrice());
    }

    @Test
    void calculateTotalPrice_NoItems() {
        assertEquals(BigDecimal.ZERO, new Order().calculateTotalPrice());
    }
}
