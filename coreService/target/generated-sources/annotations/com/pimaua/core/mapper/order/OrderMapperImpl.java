package com.pimaua.core.mapper.order;

import com.pimaua.core.dto.order.OrderCreateDto;
import com.pimaua.core.dto.order.OrderItemRequestDto;
import com.pimaua.core.dto.order.OrderItemResponseDto;
import com.pimaua.core.dto.order.OrderResponseDto;
import com.pimaua.core.dto.order.OrderUpdateDto;
import com.pimaua.core.entity.order.Order;
import com.pimaua.core.entity.order.OrderItem;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-29T12:41:27+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderResponseDto toDto(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderResponseDto.OrderResponseDtoBuilder orderResponseDto = OrderResponseDto.builder();

        orderResponseDto.id( order.getId() );
        orderResponseDto.userId( order.getUserId() );
        orderResponseDto.restaurantId( order.getRestaurantId() );
        orderResponseDto.orderStatus( order.getOrderStatus() );
        orderResponseDto.totalPrice( order.getTotalPrice() );
        orderResponseDto.createdAt( order.getCreatedAt() );
        orderResponseDto.pickupAddress( order.getPickupAddress() );
        orderResponseDto.pickupLatitude( order.getPickupLatitude() );
        orderResponseDto.pickupLongitude( order.getPickupLongitude() );
        orderResponseDto.dropOffAddress( order.getDropOffAddress() );
        orderResponseDto.dropOffLatitude( order.getDropOffLatitude() );
        orderResponseDto.dropOffLongitude( order.getDropOffLongitude() );
        orderResponseDto.orderItems( orderItemListToOrderItemResponseDtoList( order.getOrderItems() ) );
        orderResponseDto.updatedAt( order.getUpdatedAt() );

        return orderResponseDto.build();
    }

    @Override
    public Order toEntity(OrderCreateDto orderCreateDto) {
        if ( orderCreateDto == null ) {
            return null;
        }

        Order.OrderBuilder order = Order.builder();

        order.restaurantId( orderCreateDto.getRestaurantId() );
        order.pickupAddress( orderCreateDto.getPickupAddress() );
        order.pickupLatitude( orderCreateDto.getPickupLatitude() );
        order.pickupLongitude( orderCreateDto.getPickupLongitude() );
        order.dropOffAddress( orderCreateDto.getDropOffAddress() );
        order.dropOffLatitude( orderCreateDto.getDropOffLatitude() );
        order.dropOffLongitude( orderCreateDto.getDropOffLongitude() );
        order.orderItems( orderItemRequestDtoListToOrderItemList( orderCreateDto.getOrderItems() ) );

        return order.build();
    }

    @Override
    public void updateEntity(Order existingOrder, OrderUpdateDto orderUpdateDto) {
        if ( orderUpdateDto == null ) {
            return;
        }

        existingOrder.setPickupAddress( orderUpdateDto.getPickupAddress() );
        existingOrder.setPickupLatitude( orderUpdateDto.getPickupLatitude() );
        existingOrder.setPickupLongitude( orderUpdateDto.getPickupLongitude() );
        existingOrder.setDropOffAddress( orderUpdateDto.getDropOffAddress() );
        existingOrder.setDropOffLatitude( orderUpdateDto.getDropOffLatitude() );
        existingOrder.setDropOffLongitude( orderUpdateDto.getDropOffLongitude() );
    }

    @Override
    public List<OrderResponseDto> toListDto(List<Order> orderList) {
        if ( orderList == null ) {
            return null;
        }

        List<OrderResponseDto> list = new ArrayList<OrderResponseDto>( orderList.size() );
        for ( Order order : orderList ) {
            list.add( toDto( order ) );
        }

        return list;
    }

    protected OrderItemResponseDto orderItemToOrderItemResponseDto(OrderItem orderItem) {
        if ( orderItem == null ) {
            return null;
        }

        OrderItemResponseDto.OrderItemResponseDtoBuilder orderItemResponseDto = OrderItemResponseDto.builder();

        orderItemResponseDto.id( orderItem.getId() );
        orderItemResponseDto.menuItemId( orderItem.getMenuItemId() );
        orderItemResponseDto.name( orderItem.getName() );
        orderItemResponseDto.quantity( orderItem.getQuantity() );
        orderItemResponseDto.unitPrice( orderItem.getUnitPrice() );
        orderItemResponseDto.totalPrice( orderItem.getTotalPrice() );

        return orderItemResponseDto.build();
    }

    protected List<OrderItemResponseDto> orderItemListToOrderItemResponseDtoList(List<OrderItem> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderItemResponseDto> list1 = new ArrayList<OrderItemResponseDto>( list.size() );
        for ( OrderItem orderItem : list ) {
            list1.add( orderItemToOrderItemResponseDto( orderItem ) );
        }

        return list1;
    }

    protected OrderItem orderItemRequestDtoToOrderItem(OrderItemRequestDto orderItemRequestDto) {
        if ( orderItemRequestDto == null ) {
            return null;
        }

        OrderItem.OrderItemBuilder orderItem = OrderItem.builder();

        orderItem.menuItemId( orderItemRequestDto.getMenuItemId() );
        orderItem.quantity( orderItemRequestDto.getQuantity() );

        return orderItem.build();
    }

    protected List<OrderItem> orderItemRequestDtoListToOrderItemList(List<OrderItemRequestDto> list) {
        if ( list == null ) {
            return null;
        }

        List<OrderItem> list1 = new ArrayList<OrderItem>( list.size() );
        for ( OrderItemRequestDto orderItemRequestDto : list ) {
            list1.add( orderItemRequestDtoToOrderItem( orderItemRequestDto ) );
        }

        return list1;
    }
}
