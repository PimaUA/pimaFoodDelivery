package com.pimaua.core.service.restaurant;

import com.pimaua.core.dto.restaurant.OpeningHoursRequestDto;
import com.pimaua.core.dto.restaurant.OpeningHoursResponseDto;
import com.pimaua.core.entity.restaurant.OpeningHours;
import com.pimaua.core.exception.custom.OpeningHoursNotFoundException;
import com.pimaua.core.mapper.restaurant.OpeningHoursMapper;
import com.pimaua.core.repository.restaurant.OpeningHoursRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OpeningHoursService {
private final OpeningHoursRepository openingHoursRepository;
private final OpeningHoursMapper openingHoursMapper;

    public OpeningHoursResponseDto create(OpeningHoursRequestDto openingHoursRequestDto) {
        OpeningHours openingHours = openingHoursMapper.toEntity(openingHoursRequestDto);
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
                .orElseThrow(() -> new OpeningHoursNotFoundException("Opening hours  not found with ID " + id));
        openingHoursMapper.updateEntity(openingHours, openingHoursRequestDto);
        OpeningHours savedOpeningHours=openingHoursRepository.save(openingHours);
        return openingHoursMapper.toDto(savedOpeningHours);
    }

    public void delete(Integer id) {
        if (!openingHoursRepository.existsById(id)) {
            throw new OpeningHoursNotFoundException("Opening hours  not found with ID " + id);
        }
        openingHoursRepository.deleteById(id);
    }
}
