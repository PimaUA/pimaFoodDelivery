package com.pimaua.core.mapper.order;

import com.pimaua.core.dto.order.OrderItemRequestDto;
import com.pimaua.core.dto.order.OrderItemResponseDto;
import com.pimaua.core.entity.order.OrderItem;
import com.pimaua.core.entity.restaurant.MenuItem;
import java.math.BigDecimal;
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
public class OrderItemMapperImpl implements OrderItemMapper {

    @Override
    public OrderItem toEntity(OrderItemRequestDto orderItemRequestDto, MenuItem menuItem, BigDecimal unitPrice, BigDecimal totalPrice) {
        if ( orderItemRequestDto == null && menuItem == null && unitPrice == null && totalPrice == null ) {
            return null;
        }

        OrderItem.OrderItemBuilder orderItem = OrderItem.builder();

        if ( orderItemRequestDto != null ) {
            orderItem.quantity( orderItemRequestDto.getQuantity() );
        }
        if ( menuItem != null ) {
            orderItem.menuItemId( menuItem.getId() );
            orderItem.name( menuItem.getName() );
            orderItem.updatedAt( menuItem.getUpdatedAt() );
        }
        orderItem.unitPrice( unitPrice );
        orderItem.totalPrice( totalPrice );

        return orderItem.build();
    }

    @Override
    public OrderItemResponseDto toDto(OrderItem orderItem) {
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

    @Override
    public void updateEntity(OrderItem orderItem, OrderItemRequestDto orderItemRequestDto, MenuItem menuItem, BigDecimal unitPrice, BigDecimal totalPrice) {
        if ( orderItemRequestDto == null && menuItem == null && unitPrice == null && totalPrice == null ) {
            return;
        }

        if ( orderItemRequestDto != null ) {
            orderItem.setMenuItemId( orderItemRequestDto.getMenuItemId() );
            orderItem.setQuantity( orderItemRequestDto.getQuantity() );
        }
        if ( menuItem != null ) {
            orderItem.setName( menuItem.getName() );
            orderItem.setUpdatedAt( menuItem.getUpdatedAt() );
        }
        orderItem.setUnitPrice( unitPrice );
        orderItem.setTotalPrice( totalPrice );
    }

    @Override
    public List<OrderItemResponseDto> toListDto(List<OrderItem> orderItems) {
        if ( orderItems == null ) {
            return null;
        }

        List<OrderItemResponseDto> list = new ArrayList<OrderItemResponseDto>( orderItems.size() );
        for ( OrderItem orderItem : orderItems ) {
            list.add( toDto( orderItem ) );
        }

        return list;
    }
}
