package com.pimaua.core.mapper.order;

import com.pimaua.core.dto.order.OrderCreateDto;
import com.pimaua.core.dto.order.OrderResponseDto;
import com.pimaua.core.dto.order.OrderUpdateDto;
import com.pimaua.core.entity.order.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    OrderResponseDto toDto(Order order);

    Order toEntity(OrderCreateDto orderCreateDto);

    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget Order existingOrder, OrderUpdateDto orderUpdateDto);

    List<OrderResponseDto> toListDto(List<Order>orderList);
}
