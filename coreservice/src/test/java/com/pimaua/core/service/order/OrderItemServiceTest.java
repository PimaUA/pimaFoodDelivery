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
import com.pimaua.core.repository.restaurant.MenuItemRepository;
import com.pimaua.core.service.order.testdata.OrderItemTestData;
import com.pimaua.core.service.restaurant.MenuItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
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
        menuItem = OrderItemTestData.mockMenuItem();
        order = OrderItemTestData.mockOrder();
        existingOrderItem = OrderItemTestData.mockOrderItem(order,menuItem);
        orderItemRequestDto = OrderItemTestData.mockOrderItemRequestDto();
        orderItemResponseDto = OrderItemTestData.mockOrderItemResponseDto();
    }

    @Test
    void addItemToExistingOrder_Success() {
        // Given: an existing order with ID 100 and a valid menu item
        when(orderRepository.findById(100)).thenReturn(Optional.of(order));
        when(menuItemService.getMenuItemById(1)).thenReturn(menuItem);
        when(orderItemMapper.toEntity(any(), any(), any(), any()))
                .thenReturn(existingOrderItem);
        when(orderItemMapper.toDto(existingOrderItem))
                .thenReturn(orderItemResponseDto);

        // When: adding the item to the existing order
        OrderItemResponseDto result =
                orderItemService.addItemToExistingOrder(100, orderItemRequestDto);

        // Then: verify the response, order updates, and interactions
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
    void addItemToExistingOrder_OrderNotFound() {
        // Given: no order exists with ID 123
        when(orderRepository.findById(123)).thenReturn(Optional.empty());

        // When & Then: adding item throws OrderNotFoundException
        assertThrows(OrderNotFoundException.class,
                () -> orderItemService.addItemToExistingOrder(123, orderItemRequestDto));

        // Then: verify repository interaction and no mapper interactions
        verify(orderRepository).findById(123);
        verifyNoInteractions(orderItemMapper);
    }

    @Test
    void addItemToExistingOrder_OrderNotPending() {
        // Given: an order that is not in PENDING status
        order.setOrderStatus(OrderStatus.DELIVERED);

        // When & Then: adding item throws IllegalStateException
        when(orderRepository.findById(100)).thenReturn(Optional.of(order));
        assertThrows(IllegalStateException.class,
                () -> orderItemService.addItemToExistingOrder(100, orderItemRequestDto));

        // Then: verify repository interaction and no mapper interactions
        verify(orderRepository).findById(100);
        verifyNoInteractions(orderItemMapper);
    }

    @Test
    void findAll_Success() {
        // Given: some existing order items and mapped DTOs
        List<OrderItem> orderItems = Arrays.asList(existingOrderItem);
        List<OrderItemResponseDto> expectedDtos = Arrays.asList(orderItemResponseDto);
        when(orderItemRepository.findAll()).thenReturn(orderItems);
        when(orderItemMapper.toListDto(orderItems)).thenReturn(expectedDtos);

        // When: fetching all order items
        List<OrderItemResponseDto> result = orderItemService.findAll();

        // Then: verify returned list and interactions
        assertEquals(expectedDtos, result);
        assertEquals(1, result.size());
        verify(orderItemRepository).findAll();
        verify(orderItemMapper).toListDto(orderItems);
    }

    @Test
    void findById_Success() {
        // Given: an existing order item with ID 1
        when(orderItemRepository.findById(1)).thenReturn(Optional.of(existingOrderItem));
        when(orderItemMapper.toDto(existingOrderItem)).thenReturn(orderItemResponseDto);

        // When: fetching order item by ID
        OrderItemResponseDto result = orderItemService.findById(1);

        // Then: verify the returned DTO and repository interaction
        assertEquals(orderItemResponseDto, result);
        verify(orderItemRepository).findById(1);
        verify(orderItemMapper).toDto(existingOrderItem);
    }

    @Test
    void findById_OrderItemNotFound() {
        // Given: no order item exists for any ID
        when(orderItemRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then: fetching throws OrderItemNotFoundException
        OrderItemNotFoundException exception = assertThrows(OrderItemNotFoundException.class,
                () -> orderItemService.findById(999));
        assertEquals("OrderItem not found with ID 999", exception.getMessage());

        // Then: verify repository interaction and no mapper interaction
        verify(orderItemRepository).findById(anyInt());
        verifyNoInteractions(orderItemMapper);
    }

    @Test
    void findByOrderId_Success() {
        // Given: existing order items for a specific order ID
        int orderId = 100;
        List<OrderItem> orderItems = Arrays.asList(existingOrderItem);
        List<OrderItemResponseDto> expectedDtos = Arrays.asList(orderItemResponseDto);
        when(orderItemRepository.findByOrderId(orderId)).thenReturn(orderItems);
        when(orderItemMapper.toListDto(orderItems)).thenReturn(expectedDtos);

        // When: fetching items by order ID
        List<OrderItemResponseDto> result = orderItemService.findByOrderId(orderId);

        // Then: verify the returned list and repository interaction
        assertNotNull(result);
        assertEquals(expectedDtos, result);
        assertEquals(1, result.size());
        verify(orderItemRepository).findByOrderId(orderId);
        verify(orderItemMapper).toListDto(orderItems);
    }

    @Test
    void findByOrderId_OrderItemNotFound() {
        // Given: no items exist for order ID 999
        int orderId = 999;
        when(orderItemRepository.findByOrderId(orderId)).thenReturn(List.of());

        // When & Then: fetching throws OrderItemNotFoundException
        OrderItemNotFoundException exception = assertThrows(OrderItemNotFoundException.class,
                () -> orderItemService.findByOrderId(orderId));

        // Then: verify repository interaction and no mapper interaction
        assertEquals("No OrderItems found for order ID " + orderId, exception.getMessage());
        verify(orderItemRepository).findByOrderId(orderId);
        verifyNoInteractions(orderItemMapper);
    }

    @Test
    void update_Success() {
        // Given: an existing order item and menu item
        BigDecimal unitPrice = menuItem.getPrice();
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(orderItemRequestDto.getQuantity()));
        when(orderItemRepository.findById(1)).thenReturn(Optional.of(existingOrderItem));
        when(menuItemService.getMenuItemById(1)).thenReturn(menuItem);
        doNothing().when(orderItemMapper).updateEntity(existingOrderItem, orderItemRequestDto, menuItem,
                unitPrice, totalPrice);
        order.setOrderStatus(OrderStatus.PENDING);
        when(orderRepository.save(order)).thenReturn(order);
        when(orderItemMapper.toDto(existingOrderItem)).thenReturn(orderItemResponseDto);

        // When: updating the order item
        OrderItemResponseDto result = orderItemService.update(1, orderItemRequestDto);

        // Then: verify updated DTO and interactions
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
        // Given: no order item exists with ID 999
        when(orderItemRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then: updating throws OrderItemNotFoundException
        OrderItemNotFoundException exception = assertThrows(OrderItemNotFoundException.class,
                () -> orderItemService.findById(999));
        assertEquals("OrderItem not found with ID 999", exception.getMessage());

        // Then: verify repository interaction and no service or mapper interaction
        verify(orderItemRepository).findById(999);
        verifyNoInteractions(menuItemService);
        verifyNoInteractions(orderItemMapper);
    }

    @Test
    void update_OrderNotPending() {
        // Given: order status is not PENDING
        existingOrderItem.getOrder().setOrderStatus(OrderStatus.DELIVERED);
        when(orderItemRepository.findById(1)).thenReturn(Optional.of(existingOrderItem));

        // When & Then: updating throws IllegalStateException
        assertThrows(IllegalStateException.class,
                () -> orderItemService.update(1, orderItemRequestDto));

        // Then: verify repository interaction only
        verify(orderItemRepository).findById(1);
        verifyNoInteractions(orderItemMapper);
    }

    @Test
    void delete_Success() {
        // Given: order with one item and recalculated total
        order.getOrderItems().add(existingOrderItem);
        existingOrderItem.setOrder(order);
        BigDecimal recalculatedTotal = BigDecimal.valueOf(15.00);
        when(orderItemRepository.findById(1)).thenReturn(Optional.of(existingOrderItem));
        when(order.calculateTotalPrice()).thenReturn(recalculatedTotal);
        order.setOrderStatus(OrderStatus.PENDING);
        when(orderRepository.save(order)).thenReturn(order);

        // When: deleting the order item
        orderItemService.delete(1);

        // Then: verify removal, total price update, and repository calls
        verify(orderItemRepository).findById(1);
        assertFalse(order.getOrderItems().contains(existingOrderItem)); // ensure removed
        verify(order).setTotalPrice(recalculatedTotal);
        verify(orderRepository).save(order);
    }

    @Test
    void delete_OrderItemNotFound() {
        // Given: no order item exists with ID 999
        when(orderItemRepository.findById(999)).thenReturn(Optional.empty());

        // When & Then: deleting throws OrderItemNotFoundException
        OrderItemNotFoundException exception = assertThrows(OrderItemNotFoundException.class,
                () -> orderItemService.findById(999));
        assertEquals("OrderItem not found with ID 999", exception.getMessage());

        // Then: verify repository interaction only
        verify(orderItemRepository).findById(999);
        verifyNoInteractions(orderRepository);
    }

    @Test
    void delete_OrderNotPending() {
        // Given: order has an item but status is not PENDING
        order.getOrderItems().add(existingOrderItem);
        existingOrderItem.setOrder(order);
        order.setOrderStatus(OrderStatus.DELIVERED);

        // When & Then: deleting throws IllegalStateException
        when(orderItemRepository.findById(1)).thenReturn(Optional.of(existingOrderItem));
        assertThrows(IllegalStateException.class, () -> orderItemService.delete(1));

        // Then: verify repository interaction only
        verify(orderItemRepository).findById(1);
        verifyNoInteractions(orderRepository);
    }

    @Test
    void testBuildOrderItem() {
        // Given: a menu item and mapper behavior
        when(menuItemService.getMenuItemById(1)).thenReturn(menuItem);
        when(orderItemMapper.toEntity(any(OrderItemRequestDto.class), eq(menuItem),
                eq(BigDecimal.valueOf(10.00)), eq(BigDecimal.valueOf(20.00))))
                .thenReturn(existingOrderItem);

        // When: building order item from request DTO
        OrderItem result = orderItemService.buildOrderItemForOrder(orderItemRequestDto);

        // Then: verify returned entity and interactions
        assertThat(result).isEqualTo(existingOrderItem);
        verify(menuItemService).getMenuItemById(1);
        verify(orderItemMapper).toEntity(orderItemRequestDto, menuItem,
                BigDecimal.valueOf(10.00), BigDecimal.valueOf(20.00));
    }

    @Test
    void buildOrderItemForOrder_InvalidQuantity() {
        // Given: invalid quantity in request
        orderItemRequestDto.setQuantity(0);

        // When & Then: building order item throws IllegalArgumentException
        assertThrows(IllegalArgumentException.class,
                () -> orderItemService.buildOrderItemForOrder(orderItemRequestDto));

        // Then: mapper should not be called
        verifyNoInteractions(orderItemMapper);
    }

    @Test
    void calculateTotalPrice_Success() {
        // Given: a menu item and multiple quantities to test
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

            // When: building order item
            OrderItem result = orderItemService.buildOrderItemForOrder(orderItemRequestDto);

            // Then: verify total price, unit price, and quantity
            assertThat(result.getTotalPrice()).isEqualByComparingTo(expectedTotal);
            assertThat(result.getUnitPrice()).isEqualByComparingTo(menuItem.getPrice());
            assertThat(result.getQuantity()).isEqualTo(quantity);
        }

        // Then: menuItemService called correct number of times
        verify(menuItemService, times(quantities.length)).getMenuItemById(1);
    }
}