package com.pimaua.core.mapper.order;

import com.pimaua.core.dto.order.OrderItemRequestDto;
import com.pimaua.core.dto.order.OrderItemResponseDto;
import com.pimaua.core.entity.order.OrderItem;
import com.pimaua.core.entity.restaurant.MenuItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.math.BigDecimal;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "menuItem.id", target = "menuItemId")
    @Mapping(source = "menuItem.name", target = "name")
    @Mapping(source = "unitPrice", target = "unitPrice")
    @Mapping(source = "totalPrice", target = "totalPrice")
    OrderItem toEntity(OrderItemRequestDto orderItemRequestDto,
                       MenuItem menuItem, BigDecimal unitPrice, BigDecimal totalPrice);

    OrderItemResponseDto toDto(OrderItem orderItem);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "unitPrice", target = "unitPrice")
    @Mapping(source = "totalPrice", target = "totalPrice")
    void updateEntity(@MappingTarget OrderItem orderItem, OrderItemRequestDto orderItemRequestDto, MenuItem menuItem,
                      BigDecimal unitPrice, BigDecimal totalPrice);

    List<OrderItemResponseDto> toListDto(List<OrderItem> orderItems);
}
