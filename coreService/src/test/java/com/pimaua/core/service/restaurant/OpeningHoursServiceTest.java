package com.pimaua.core.service.restaurant;

import com.pimaua.core.dto.restaurant.OpeningHoursRequestDto;
import com.pimaua.core.dto.restaurant.OpeningHoursResponseDto;
import com.pimaua.core.entity.enums.DayOfWeek;
import com.pimaua.core.entity.restaurant.OpeningHours;
import com.pimaua.core.entity.restaurant.Restaurant;
import com.pimaua.core.exception.custom.notfound.OpeningHoursNotFoundException;
import com.pimaua.core.mapper.restaurant.OpeningHoursMapper;
import com.pimaua.core.repository.restaurant.OpeningHoursRepository;
import com.pimaua.core.repository.restaurant.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OpeningHoursServiceTest {
    @Mock
    private OpeningHoursRepository openingHoursRepository;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private OpeningHoursMapper openingHoursMapper;

    @InjectMocks
    private OpeningHoursService openingHoursService;

    private OpeningHours openingHours;
    private OpeningHoursRequestDto openingHoursRequestDto;
    private OpeningHoursResponseDto openingHoursResponseDto;
    private Restaurant restaurant;

    @BeforeEach
    void setup() {
        restaurant = Restaurant.builder()
                .id(1)
                .name("Some Restaurant")
                .address("Some Address")
                .build();

        openingHours = OpeningHours.builder()
                .id(1)
                .dayOfWeek(DayOfWeek.MONDAY)
                .opensAt(LocalTime.of(9, 0))
                .closesAt(LocalTime.of(22, 0))
                .is24Hours(false)
                .updatedAt(LocalDateTime.now())
                .restaurant(restaurant)
                .build();

        openingHoursRequestDto = OpeningHoursRequestDto.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .opensAt(LocalTime.of(9, 0))
                .closesAt(LocalTime.of(22, 0))
                .is24Hours(false)
                .restaurantId(restaurant.getId())
                .build();

        openingHoursResponseDto = OpeningHoursResponseDto.builder()
                .id(1)
                .dayOfWeek(DayOfWeek.MONDAY)
                .opensAt(LocalTime.of(9, 0))
                .closesAt(LocalTime.of(22, 0))
                .is24Hours(false)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    //CreateOpeningHours Tests
    @Test
    void createOpeningHours_Success() {
        //given
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(openingHoursMapper.toEntity(openingHoursRequestDto)).thenReturn(openingHours);
        when(openingHoursRepository.save(openingHours)).thenReturn(openingHours);
        when(openingHoursMapper.toDto(openingHours)).thenReturn(openingHoursResponseDto);
        //when
        OpeningHoursResponseDto result = openingHoursService.create(1, openingHoursRequestDto);
        //then
        assertNotNull(result);
        assertEquals(openingHoursResponseDto.getId(), result.getId());
        assertEquals(openingHoursResponseDto.getIs24Hours(), result.getIs24Hours());

        // interaction verification
        verify(openingHoursMapper).toEntity(openingHoursRequestDto);
        verify(openingHoursRepository).save(openingHours);
        verify(openingHoursMapper).toDto(openingHours);
    }

    @Test
    void createOpeningHours_RepositoryException() {
        //given
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(openingHoursMapper.toEntity(openingHoursRequestDto)).thenReturn(openingHours);
        when(openingHoursRepository.save(openingHours)).thenThrow(new RuntimeException("Database error"));
        //when&then
        assertThrows(RuntimeException.class, () -> {
            openingHoursService.create(1, openingHoursRequestDto);
        });
    }

    //find all tests
    @Test
    void findByRestaurantId_Success() {
        // given
        Integer restaurantId = 1;
        List<OpeningHours> openingHoursList = List.of(openingHours);
        List<OpeningHoursResponseDto> responseDtos = List.of(openingHoursResponseDto);
        when(openingHoursRepository.findByRestaurantId(restaurantId)).thenReturn(openingHoursList);
        when(openingHoursMapper.toListDto(openingHoursList)).thenReturn(responseDtos);
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        // when
        List<OpeningHoursResponseDto> result = openingHoursService.findByRestaurantId(restaurantId);
        // then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(responseDtos, result);
    }

    @Test
    void findByRestaurantId_EmptyList() {
        // given
        Integer restaurantId = 1;
        List<OpeningHours> emptyList = List.of();
        List<OpeningHoursResponseDto> emptyDtos = List.of();
        when(openingHoursRepository.findByRestaurantId(restaurantId)).thenReturn(emptyList);
        when(openingHoursMapper.toListDto(emptyList)).thenReturn(emptyDtos);
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        // when
        List<OpeningHoursResponseDto> result = openingHoursService.findByRestaurantId(restaurantId);
        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    //findById tests
    @Test
    void findById_Success() {
        // given
        Integer restaurantId = 1;
        List<OpeningHours> emptyList = List.of();
        List<OpeningHoursResponseDto> emptyDtos = List.of();
        when(openingHoursRepository.findByRestaurantId(restaurantId)).thenReturn(emptyList);
        when(openingHoursMapper.toListDto(emptyList)).thenReturn(emptyDtos);
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        // when
        List<OpeningHoursResponseDto> result = openingHoursService.findByRestaurantId(restaurantId);
        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findById_OpeningHoursNotFound() {
        //given
        when(openingHoursRepository.findById(anyInt())).thenReturn(Optional.empty());
        //when&then
        OpeningHoursNotFoundException exception = assertThrows(OpeningHoursNotFoundException.class, () -> {
            openingHoursService.findById(999);
        });
        assertEquals("Opening hours not found with ID 999", exception.getMessage());
    }

    //update OpeningHours tests
    @Test
    void updateOpeningHours_Success() {
        // Given
        when(openingHoursRepository.findById(1)).thenReturn(Optional.of(openingHours));
        when(openingHoursRepository.save(openingHours)).thenReturn(openingHours);
        when(openingHoursMapper.toDto(openingHours)).thenReturn(openingHoursResponseDto);
        // When
        OpeningHoursResponseDto result = openingHoursService.update(1, openingHoursRequestDto);
        // Then
        assertNotNull(result);
        assertEquals(openingHoursResponseDto, result);
        // interaction verification
        verify(openingHoursRepository).findById(1);
        verify(openingHoursRepository).save(openingHours);
        verify(openingHoursMapper).toDto(openingHours);
    }

    @Test
    void updateOpeningHours_OpeningHoursNotFound() {
        // Given
        when(openingHoursRepository.findById(anyInt())).thenReturn(Optional.empty());
        // When & Then
        OpeningHoursNotFoundException exception = assertThrows(OpeningHoursNotFoundException.class, () -> {
            openingHoursService.update(999, openingHoursRequestDto);
        });
        assertEquals("Opening hours not found with ID 999", exception.getMessage());
    }

    @Test
    void updateOpeningHours_RepositoryException() {
        // Given
        when(openingHoursRepository.findById(1)).thenReturn(Optional.of(openingHours));
        when(openingHoursRepository.save(openingHours)).thenThrow(new RuntimeException("Database error"));
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            openingHoursService.update(1, openingHoursRequestDto);
        });
    }

    //delete OpeningHours tests
    @Test
    void deleteOpeningHours_Success() {
        // Given
        when(openingHoursRepository.findById(1)).thenReturn(Optional.of(openingHours));
        // When
        openingHoursService.delete(1);
        // Then
        verify(openingHoursRepository).findById(1);
        verify(openingHoursRepository).delete(openingHours);
    }

    @Test
    void deleteOpeningHours_OpeningHoursNotFound() {
        // Given
        when(openingHoursRepository.findById(anyInt())).thenReturn(Optional.empty());
        // When & Then
        OpeningHoursNotFoundException exception = assertThrows(OpeningHoursNotFoundException.class, () -> {
            openingHoursService.delete(999);
        });
        assertEquals("Opening hours not found with ID 999", exception.getMessage());
        verify(openingHoursRepository).findById(999);
        verify(openingHoursRepository, never()).delete(any());
    }

    @Test
    void deleteOpeningHours_RepositoryException() {
        // Given
        when(openingHoursRepository.findById(1)).thenReturn(Optional.of(openingHours));
        doThrow(new RuntimeException("Database error")).when(openingHoursRepository).delete(openingHours);
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            openingHoursService.delete(1);
        });

        verify(openingHoursRepository).findById(1);
        verify(openingHoursRepository).delete(openingHours);
    }

    //edge cases
    @Test
    void createOpeningHours_NullInput_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> openingHoursService.create(1,null));
    }
}
