package com.pimaua.core.mapper.restaurant;

import com.pimaua.core.dto.restaurant.RestaurantRequestDto;
import com.pimaua.core.dto.restaurant.RestaurantResponseDto;
import com.pimaua.core.entity.restaurant.Restaurant;
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
public class RestaurantMapperImpl implements RestaurantMapper {

    @Override
    public Restaurant toEntity(RestaurantRequestDto restaurantRequestDto) {
        if ( restaurantRequestDto == null ) {
            return null;
        }

        Restaurant.RestaurantBuilder restaurant = Restaurant.builder();

        restaurant.name( restaurantRequestDto.getName() );
        restaurant.description( restaurantRequestDto.getDescription() );
        restaurant.address( restaurantRequestDto.getAddress() );
        restaurant.isActive( restaurantRequestDto.getIsActive() );

        return restaurant.build();
    }

    @Override
    public RestaurantResponseDto toDto(Restaurant restaurant) {
        if ( restaurant == null ) {
            return null;
        }

        RestaurantResponseDto.RestaurantResponseDtoBuilder restaurantResponseDto = RestaurantResponseDto.builder();

        restaurantResponseDto.id( restaurant.getId() );
        restaurantResponseDto.name( restaurant.getName() );
        restaurantResponseDto.description( restaurant.getDescription() );
        restaurantResponseDto.address( restaurant.getAddress() );
        restaurantResponseDto.isActive( restaurant.getIsActive() );
        restaurantResponseDto.updatedAt( restaurant.getUpdatedAt() );

        return restaurantResponseDto.build();
    }

    @Override
    public void updateEntity(Restaurant restaurant, RestaurantRequestDto restaurantRequestDto) {
        if ( restaurantRequestDto == null ) {
            return;
        }

        restaurant.setName( restaurantRequestDto.getName() );
        restaurant.setDescription( restaurantRequestDto.getDescription() );
        restaurant.setAddress( restaurantRequestDto.getAddress() );
        restaurant.setIsActive( restaurantRequestDto.getIsActive() );
    }

    @Override
    public List<RestaurantResponseDto> toListDto(List<Restaurant> restaurants) {
        if ( restaurants == null ) {
            return null;
        }

        List<RestaurantResponseDto> list = new ArrayList<RestaurantResponseDto>( restaurants.size() );
        for ( Restaurant restaurant : restaurants ) {
            list.add( toDto( restaurant ) );
        }

        return list;
    }
}
