package com.pimaua.core.mapper.order;

import com.pimaua.core.dto.order.OrderCreateDto;
import com.pimaua.core.dto.order.OrderResponseDto;
import com.pimaua.core.dto.order.OrderUpdateDto;
import com.pimaua.core.entity.order.Order;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    OrderResponseDto toDto(Order order);

    Order toEntity(OrderCreateDto orderCreateDto);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget Order existingOrder, OrderUpdateDto orderUpdateDto);

    List<OrderResponseDto> toListDto(List<Order>orderList);

    default Page<OrderResponseDto> toPageDto(Page<Order>orderList){
        return orderList.map(this::toDto);
    };
}
