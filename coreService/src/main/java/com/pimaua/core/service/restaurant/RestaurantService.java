package com.pimaua.core.service.restaurant;

import com.pimaua.core.dto.restaurant.RestaurantRequestDto;
import com.pimaua.core.dto.restaurant.RestaurantResponseDto;
import com.pimaua.core.entity.restaurant.Restaurant;
import com.pimaua.core.exception.custom.notfound.RestaurantNotFoundException;
import com.pimaua.core.mapper.restaurant.RestaurantMapper;
import com.pimaua.core.repository.restaurant.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;
    private static final Logger logger = LoggerFactory.getLogger(RestaurantService.class);

    public RestaurantResponseDto create(RestaurantRequestDto restaurantRequestDto) {
        if (restaurantRequestDto == null) {
            logger.error("Failed to create restaurant, no input");
            throw new IllegalArgumentException("RestaurantRequestDto cannot be null");
        }
        Restaurant restaurant = restaurantMapper.toEntity(restaurantRequestDto);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toDto(savedRestaurant);
    }

    @Transactional(readOnly = true)
    public List<RestaurantResponseDto> findAll() {
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        return restaurantMapper.toListDto(restaurantList);
    }

    @Transactional(readOnly = true)
    public RestaurantResponseDto findById(Integer id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Restaurant not found with id={}", id);
                    return new RestaurantNotFoundException("Restaurant not found with ID " + id);
                });
        return restaurantMapper.toDto(restaurant);
    }

    public RestaurantResponseDto update(Integer id, RestaurantRequestDto restaurantRequestDto) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Restaurant not found with id={}", id);
                    return new RestaurantNotFoundException("Restaurant not found with ID " + id);
                });
        restaurantMapper.updateEntity(restaurant, restaurantRequestDto);
        Restaurant savedRestaurant = restaurantRepository.save(restaurant);
        return restaurantMapper.toDto(savedRestaurant);
    }

    public void delete(Integer id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Restaurant not found with id={}", id);
                    return new RestaurantNotFoundException("Restaurant not found with ID " + id);
                });
        restaurantRepository.delete(restaurant);
    }
}
