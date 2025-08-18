package com.pimaua.core.service.order;

import com.pimaua.core.dto.order.OrderItemRequestDto;
import com.pimaua.core.dto.order.OrderItemResponseDto;
import com.pimaua.core.entity.enums.OrderStatus;
import com.pimaua.core.entity.order.Order;
import com.pimaua.core.entity.order.OrderItem;
import com.pimaua.core.entity.restaurant.MenuItem;
import com.pimaua.core.exception.custom.notfound.OrderItemNotFoundException;
import com.pimaua.core.mapper.order.OrderItemMapper;
import com.pimaua.core.repository.order.OrderItemRepository;
import com.pimaua.core.repository.order.OrderRepository;
import com.pimaua.core.repository.restaurant.MenuItemRepository;
import com.pimaua.core.service.restaurant.MenuItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderItemServiceTest {
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemMapper orderItemMapper;
    @Mock
    private MenuItemService menuItemService;
    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private OrderItemService orderItemService;

    private OrderItemRequestDto orderItemRequestDto;
    private OrderItem existingOrderItem;
    private OrderItemResponseDto orderItemResponseDto;
    private MenuItem menuItem;
    private Order order;


    @BeforeEach
    void setUp() {
        menuItem = MenuItem.builder()
                .id(1)
                .price(BigDecimal.valueOf(10.0))
                .name("Menu Item")
                .build();

        order = spy(Order.builder()
                .userId(1)
                .restaurantId(2)
                .orderStatus(OrderStatus.PENDING)
                .totalPrice(BigDecimal.valueOf(20.0))
                .createdAt(LocalDateTime.of(2025, 7, 30, 12, 0))
                .pickupAddress("Some street")
                .pickupLatitude(BigDecimal.valueOf(40.1234))
                .pickupLongitude(BigDecimal.valueOf(40.1234))
                .dropOffAddress("Another street")
                .dropOffLatitude(BigDecimal.valueOf(50.1234))
                .dropOffLongitude(BigDecimal.valueOf(50.1234))
                .orderItems(new ArrayList<>())
                .build());

        existingOrderItem = OrderItem.builder()
                .id(1)
                .menuItemId(1)
                .name("Menu Item")
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(10.00))
                .totalPrice(BigDecimal.valueOf(20.00))
                .order(order)
                .updatedAt(LocalDateTime.now())
                .build();

        OrderItem updatedOrderItem = OrderItem.builder()
                .id(1)
                .menuItemId(1)
                .name("Updated Item")
                .quantity(3)
                .unitPrice(BigDecimal.valueOf(12.00))
                .totalPrice(BigDecimal.valueOf(36.00))
                .order(order)
                .updatedAt(LocalDateTime.now())
                .build();

        orderItemRequestDto = OrderItemRequestDto.builder()
                .menuItemId(1)
                .quantity(2)
                .build();

        orderItemResponseDto = OrderItemResponseDto.builder()
                .id(1)
                .menuItemId(1)
                .name("Menu Item")
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(10.00))
                .totalPrice(BigDecimal.valueOf(20.00))
                .build();
    }

    @Test
    void addItemToExistingOrder_Success() {
        // given
        when(orderRepository.findById(100)).thenReturn(Optional.of(order));
        when(menuItemService.getMenuItemById(1)).thenReturn(menuItem);
        when(orderItemMapper.toEntity(any(), any(), any(), any()))
                .thenReturn(existingOrderItem);
        when(orderItemMapper.toDto(existingOrderItem))
                .thenReturn(orderItemResponseDto);
        // when
        OrderItemResponseDto result =
                orderItemService.addItemToExistingOrder(100, orderItemRequestDto);
        // then
        assertNotNull(result);
        assertEquals(orderItemResponseDto, result);
        assertEquals(1, order.getOrderItems().size());
        assertEquals(existingOrderItem, order.getOrderItems().get(0));
        assertEquals(BigDecimal.valueOf(20.00), order.getTotalPrice());
        verify(orderRepository).save(order);
        verify(orderItemMapper).toEntity(any(), any(), any(), any());
        verify(orderItemMapper).toDto(existingOrderItem);
    }

    @Test
    void findAll_Success() {
        // Given
        List<OrderItem> orderItems = Arrays.asList(existingOrderItem);
        List<OrderItemResponseDto> expectedDtos = Arrays.asList(orderItemResponseDto);
        when(orderItemRepository.findAll()).thenReturn(orderItems);
        when(orderItemMapper.toListDto(orderItems)).thenReturn(expectedDtos);
        // When
        List<OrderItemResponseDto> result = orderItemService.findAll();
        // Then
        assertEquals(expectedDtos, result);
        assertEquals(1, result.size());
        verify(orderItemRepository).findAll();
        verify(orderItemMapper).toListDto(orderItems);
    }

    @Test
    void findById_Success() {
        // Given
        when(orderItemRepository.findById(1)).thenReturn(Optional.of(existingOrderItem));
        when(orderItemMapper.toDto(existingOrderItem)).thenReturn(orderItemResponseDto);
        // When
        OrderItemResponseDto result = orderItemService.findById(1);
        // Then
        assertEquals(orderItemResponseDto, result);
        verify(orderItemRepository).findById(1);
        verify(orderItemMapper).toDto(existingOrderItem);
    }

    @Test
    void findById_OrderItemNotFound() {
        // Given
        when(orderItemRepository.findById(anyInt())).thenReturn(Optional.empty());
        // When & Then
        OrderItemNotFoundException exception = assertThrows(OrderItemNotFoundException.class,
                () -> orderItemService.findById(999));
        assertEquals("OrderItem not found with ID 999", exception.getMessage());

        verify(orderItemRepository).findById(anyInt());
        verifyNoInteractions(orderItemMapper);
    }

    @Test
    void findByOrderId_Success() {
        // Given
        int orderId = 100;
        List<OrderItem> orderItems = Arrays.asList(existingOrderItem);
        List<OrderItemResponseDto> expectedDtos = Arrays.asList(orderItemResponseDto);
        when(orderItemRepository.findByOrderId(orderId)).thenReturn(orderItems);
        when(orderItemMapper.toListDto(orderItems)).thenReturn(expectedDtos);
        // When
        List<OrderItemResponseDto> result = orderItemService.findByOrderId(orderId);
        // Then
        assertNotNull(result);
        assertEquals(expectedDtos, result);
        assertEquals(1, result.size());
        verify(orderItemRepository).findByOrderId(orderId);
        verify(orderItemMapper).toListDto(orderItems);
    }

    @Test
    void findByOrderId_OrderItemNotFound() {
        // Given
        int orderId = 999;
        when(orderItemRepository.findByOrderId(orderId)).thenReturn(List.of());

        // When & Then
        OrderItemNotFoundException exception = assertThrows(OrderItemNotFoundException.class,
                () -> orderItemService.findByOrderId(orderId));

        assertEquals("No OrderItems found for order ID " + orderId, exception.getMessage());
        verify(orderItemRepository).findByOrderId(orderId);
        verifyNoInteractions(orderItemMapper);
    }

    @Test
    void update_Success() {
        // given
        BigDecimal unitPrice = menuItem.getPrice();
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(orderItemRequestDto.getQuantity()));
        when(orderItemRepository.findById(1)).thenReturn(Optional.of(existingOrderItem));
        when(menuItemService.getMenuItemById(1)).thenReturn(menuItem);
        doNothing().when(orderItemMapper).updateEntity(existingOrderItem, orderItemRequestDto, menuItem,
                unitPrice, totalPrice);
        order.setOrderStatus(OrderStatus.PENDING);
        when(orderRepository.save(order)).thenReturn(order);

        when(orderItemMapper.toDto(existingOrderItem)).thenReturn(orderItemResponseDto);
        // when
        OrderItemResponseDto result = orderItemService.update(1, orderItemRequestDto);
        // then
        assertEquals(orderItemResponseDto, result);
        verify(orderItemRepository).findById(1);
        verify(menuItemService).getMenuItemById(1);
        verify(orderItemMapper).updateEntity(existingOrderItem, orderItemRequestDto, menuItem,
                BigDecimal.valueOf(10.00), BigDecimal.valueOf(20.00));
        verify(orderRepository).save(order);
        verify(orderItemMapper).toDto(existingOrderItem);
    }

    @Test
    void update_OrderItemNotFound() {
        // Given
        when(orderItemRepository.findById(999)).thenReturn(Optional.empty());
        // When & Then
        OrderItemNotFoundException exception = assertThrows(OrderItemNotFoundException.class,
                () -> orderItemService.findById(999));
        assertEquals("OrderItem not found with ID 999", exception.getMessage());
        verify(orderItemRepository).findById(999);
        verifyNoInteractions(menuItemService);
        verifyNoInteractions(orderItemMapper);
    }

    @Test
    void delete_Success() {
        // Given
        order.getOrderItems().add(existingOrderItem);
        existingOrderItem.setOrder(order);
        BigDecimal recalculatedTotal = BigDecimal.valueOf(15.00);
        when(orderItemRepository.findById(1)).thenReturn(Optional.of(existingOrderItem));
        when(order.calculateTotalPrice()).thenReturn(recalculatedTotal);
        order.setOrderStatus(OrderStatus.PENDING);
        when(orderRepository.save(order)).thenReturn(order);
        // When
        orderItemService.delete(1);
        // Then
        verify(orderItemRepository).findById(1);
        assertFalse(order.getOrderItems().contains(existingOrderItem)); // ensure removed
        verify(order).setTotalPrice(recalculatedTotal);
        verify(orderRepository).save(order);
    }

    @Test
    void delete_OrderItemNotFound() {
        // Given
        when(orderItemRepository.findById(999)).thenReturn(Optional.empty());
        // When & Then
        OrderItemNotFoundException exception = assertThrows(OrderItemNotFoundException.class,
                () -> orderItemService.findById(999));
        assertEquals("OrderItem not found with ID 999", exception.getMessage());
        verify(orderItemRepository).findById(999);
        verifyNoInteractions(orderRepository);
    }

    @Test
    void testBuildOrderItem() {
        // Given
        when(menuItemService.getMenuItemById(1)).thenReturn(menuItem);
        when(orderItemMapper.toEntity(any(OrderItemRequestDto.class), eq(menuItem),
                eq(BigDecimal.valueOf(10.00)), eq(BigDecimal.valueOf(20.00))))
                .thenReturn(existingOrderItem);
        // When
        OrderItem result = orderItemService.buildOrderItemForOrder(orderItemRequestDto);
        // Then
        assertThat(result).isEqualTo(existingOrderItem);
        verify(menuItemService).getMenuItemById(1);
        verify(orderItemMapper).toEntity(orderItemRequestDto, menuItem,
                BigDecimal.valueOf(10.00), BigDecimal.valueOf(20.00));
    }

    @Test
    void calculateTotalPrice_Success() {
// Given
        when(menuItemService.getMenuItemById(1)).thenReturn(menuItem);
        int[] quantities = {1, 3, 5};
        for (int quantity : quantities) {
            orderItemRequestDto.setQuantity(quantity);
            BigDecimal expectedTotal = menuItem.getPrice().multiply(BigDecimal.valueOf(quantity));
            OrderItem expectedOrderItem = OrderItem.builder()
                    .menuItemId(1)
                    .quantity(quantity)
                    .unitPrice(menuItem.getPrice())
                    .totalPrice(expectedTotal)
                    .build();
            when(orderItemMapper.toEntity(eq(orderItemRequestDto), eq(menuItem),
                    eq(menuItem.getPrice()), eq(expectedTotal)))
                    .thenReturn(expectedOrderItem);
            // When
            OrderItem result = orderItemService.buildOrderItemForOrder(orderItemRequestDto);
            // Then
            assertThat(result.getTotalPrice()).isEqualByComparingTo(expectedTotal);
            assertThat(result.getUnitPrice()).isEqualByComparingTo(menuItem.getPrice());
            assertThat(result.getQuantity()).isEqualTo(quantity);
        }
        verify(menuItemService, times(quantities.length)).getMenuItemById(1);
    }
}