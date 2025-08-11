package com.pimaua.core.mapper.restaurant;

import com.pimaua.core.dto.restaurant.RestaurantRequestDto;
import com.pimaua.core.dto.restaurant.RestaurantResponseDto;
import com.pimaua.core.entity.restaurant.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantMapper {
    Restaurant toEntity(RestaurantRequestDto restaurantRequestDto);

    RestaurantResponseDto toDto(Restaurant restaurant);

    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget Restaurant restaurant, RestaurantRequestDto restaurantRequestDto);

    List<RestaurantResponseDto> toListDto(List<Restaurant> restaurants);
}
