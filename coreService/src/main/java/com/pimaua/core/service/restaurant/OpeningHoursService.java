package com.pimaua.core.service.restaurant;

import com.pimaua.core.dto.restaurant.OpeningHoursRequestDto;
import com.pimaua.core.dto.restaurant.OpeningHoursResponseDto;
import com.pimaua.core.entity.restaurant.OpeningHours;
import com.pimaua.core.entity.restaurant.Restaurant;
import com.pimaua.core.exception.custom.notfound.OpeningHoursNotFoundException;
import com.pimaua.core.exception.custom.notfound.RestaurantNotFoundException;
import com.pimaua.core.mapper.restaurant.OpeningHoursMapper;
import com.pimaua.core.repository.restaurant.OpeningHoursRepository;
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
public class OpeningHoursService {
    private final OpeningHoursRepository openingHoursRepository;
    private final RestaurantRepository restaurantRepository;
    private final OpeningHoursMapper openingHoursMapper;
    private static final Logger logger = LoggerFactory.getLogger(OpeningHoursService.class);

    public OpeningHoursResponseDto create
            (Integer restaurantId, OpeningHoursRequestDto openingHoursRequestDto) {
        if (openingHoursRequestDto == null) {
            logger.error("Failed to create OpeningHours, no input");
            throw new IllegalArgumentException("OpeningHoursRequestDto cannot be null");
        }
        Restaurant restaurant = findRestaurantByIdOrThrow(restaurantId);
        OpeningHours openingHours = openingHoursMapper.toEntity(openingHoursRequestDto);
        openingHours.setRestaurant(restaurant);
        OpeningHours savedOpeningHours = openingHoursRepository.save(openingHours);
        return openingHoursMapper.toDto(savedOpeningHours);
    }

    @Transactional(readOnly = true)
    public List<OpeningHoursResponseDto> findByRestaurantId
            (Integer restaurantId) {
        Restaurant restaurant = findRestaurantByIdOrThrow(restaurantId);
        List<OpeningHours> openingHours = openingHoursRepository.findByRestaurantId(restaurantId);
        return openingHoursMapper.toListDto(openingHours);
    }

    @Transactional(readOnly = true)
    public OpeningHoursResponseDto findById(Integer id) {
        OpeningHours openingHours = findOpeningHoursOrThrow(id);
        return openingHoursMapper.toDto(openingHours);
    }

    public OpeningHoursResponseDto update(Integer id, OpeningHoursRequestDto openingHoursRequestDto) {
        if (openingHoursRequestDto == null) {
            logger.error("Failed to update OpeningHours, no input");
            throw new IllegalArgumentException("OpeningHoursRequestDto cannot be null");
        }
        OpeningHours openingHours = findOpeningHoursOrThrow(id);
        openingHoursMapper.updateEntity(openingHours, openingHoursRequestDto);
        OpeningHours savedOpeningHours = openingHoursRepository.save(openingHours);
        return openingHoursMapper.toDto(savedOpeningHours);
    }

    public void delete(Integer id) {
        OpeningHours openingHours = findOpeningHoursOrThrow(id);
        openingHoursRepository.delete(openingHours);
    }

    private Restaurant findRestaurantByIdOrThrow(Integer restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> {
                    logger.error("Restaurant not found with id={}", restaurantId);
                    return new RestaurantNotFoundException(
                            "Restaurant not found with ID " + restaurantId);
                });
    }

    private OpeningHours findOpeningHoursOrThrow(Integer id) {
        return openingHoursRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("OpeningHours not found with id={}", id);
                    return new OpeningHoursNotFoundException("Opening hours not found with ID " + id);
                });
    }
}
