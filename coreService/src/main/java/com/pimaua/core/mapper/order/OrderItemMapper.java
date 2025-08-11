package com.pimaua.core.mapper.order;

import com.pimaua.core.dto.order.OrderItemRequestDto;
import com.pimaua.core.dto.order.OrderItemResponseDto;
import com.pimaua.core.entity.order.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderItemMapper {
    OrderItem toEntity(OrderItemRequestDto orderItemRequestDto);

    OrderItemResponseDto toDto(OrderItem orderItem);

    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget OrderItem orderItem, OrderItemRequestDto orderItemRequestDto);

    List<OrderItemResponseDto> toListDto(List<OrderItem> orderItem);
}
