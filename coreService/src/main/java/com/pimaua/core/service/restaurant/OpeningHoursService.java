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

    public OpeningHoursResponseDto create(OpeningHoursRequestDto openingHoursRequestDto) {
        if (openingHoursRequestDto == null) {
            throw new IllegalArgumentException("OpeningHoursRequestDto cannot be null");
        }
        Restaurant restaurant = restaurantRepository.findById(openingHoursRequestDto.getRestaurantId())
                .orElseThrow(() -> new RestaurantNotFoundException(
                        "Restaurant not found with ID " + openingHoursRequestDto.getRestaurantId()));

        OpeningHours openingHours = openingHoursMapper.toEntity(openingHoursRequestDto);
        openingHours.setRestaurant(restaurant);
        OpeningHours savedOpeningHours = openingHoursRepository.save(openingHours);
        return openingHoursMapper.toDto(savedOpeningHours);
    }

    public List<OpeningHoursResponseDto> findAll() {
        List<OpeningHours> openingHours = openingHoursRepository.findAll();
        return openingHoursMapper.toListDto(openingHours);
    }

    public OpeningHoursResponseDto findById(Integer id) {
        OpeningHours openingHours = openingHoursRepository.findById(id)
                .orElseThrow(() -> new OpeningHoursNotFoundException("Opening hours not found with ID " + id));
        return openingHoursMapper.toDto(openingHours);
    }

    public OpeningHoursResponseDto update(Integer id, OpeningHoursRequestDto openingHoursRequestDto) {
        OpeningHours openingHours = openingHoursRepository.findById(id)
                .orElseThrow(() -> new OpeningHoursNotFoundException("Opening hours not found with ID " + id));
        openingHoursMapper.updateEntity(openingHours, openingHoursRequestDto);
        OpeningHours savedOpeningHours = openingHoursRepository.save(openingHours);
        return openingHoursMapper.toDto(savedOpeningHours);
    }

    public void delete(Integer id) {
        OpeningHours openingHours = openingHoursRepository.findById(id)
                .orElseThrow(() -> new OpeningHoursNotFoundException("Opening hours not found with ID " + id));
        openingHoursRepository.delete(openingHours);
    }
}
