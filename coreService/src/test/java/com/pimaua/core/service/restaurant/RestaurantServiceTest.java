package com.pimaua.core.service.restaurant;

import com.pimaua.core.dto.restaurant.RestaurantRequestDto;
import com.pimaua.core.dto.restaurant.RestaurantResponseDto;
import com.pimaua.core.entity.enums.DayOfWeek;
import com.pimaua.core.entity.restaurant.Menu;
import com.pimaua.core.entity.restaurant.OpeningHours;
import com.pimaua.core.entity.restaurant.Restaurant;
import com.pimaua.core.exception.custom.notfound.RestaurantNotFoundException;
import com.pimaua.core.mapper.restaurant.RestaurantMapper;
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
public class RestaurantServiceTest {
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private RestaurantMapper restaurantMapper;

    @InjectMocks
    private RestaurantService restaurantService;

    private Restaurant restaurant;
    private RestaurantRequestDto restaurantRequestDto;
    private RestaurantResponseDto restaurantResponseDto;

    @BeforeEach
    void setup() {
        List<Menu> mockMenus = List.of(
                Menu.builder()
                        .id(1)
                        .name("Lunch Menu")
                        .build(),
                Menu.builder()
                        .id(2)
                        .name("Dinner Menu")
                        .build()
        );

        List<OpeningHours> mockOpeningHours = List.of(
                OpeningHours.builder()
                        .id(1)
                        .dayOfWeek(DayOfWeek.MONDAY)
                        .opensAt(LocalTime.of(9, 0))
                        .closesAt(LocalTime.of(21, 0))
                        .is24Hours(false)
                        .build(),
                OpeningHours.builder()
                        .id(2)
                        .dayOfWeek(DayOfWeek.TUESDAY)
                        .opensAt(LocalTime.of(10, 0))
                        .closesAt(LocalTime.of(22, 0))
                        .is24Hours(false)
                        .build()
        );

        restaurant = Restaurant.builder()
                .id(1)
                .name("Some Restaurant")
                .description("Asian Food")
                .address("Some Address")
                .isActive(true)
                .menus(mockMenus)
                .openingHours(mockOpeningHours)
                .updatedAt(LocalDateTime.now())
                .build();

        restaurantRequestDto = RestaurantRequestDto.builder()
                .name("Some Restaurant")
                .description("Asian Food")
                .address("Some Address")
                .isActive(true)
                .build();

        restaurantResponseDto = RestaurantResponseDto.builder()
                .id(1)
                .name("Some Restaurant")
                .description("Asian Food")
                .address("Some Address")
                .isActive(true)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    //CreateRestaurantTests
    @Test
    void createRestaurant_Success() {
        //given
        when(restaurantMapper.toEntity(restaurantRequestDto)).thenReturn(restaurant);
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);
        when(restaurantMapper.toDto(restaurant)).thenReturn(restaurantResponseDto);
        //when
        RestaurantResponseDto result = restaurantService.create(restaurantRequestDto);
        //then
        assertNotNull(result);
        assertEquals(restaurantResponseDto.getId(), result.getId());
        assertEquals(restaurantResponseDto.getName(), result.getName());

        // interaction verification
        verify(restaurantMapper).toEntity(restaurantRequestDto);
        verify(restaurantRepository).save(restaurant);
        verify(restaurantMapper).toDto(restaurant);
    }

    @Test
    void createRestaurant_RepositoryException() {
        //given
        when(restaurantMapper.toEntity(restaurantRequestDto)).thenReturn(restaurant);
        when(restaurantRepository.save(restaurant)).thenThrow(new RuntimeException("Database error"));
        //when&then
        assertThrows(RuntimeException.class, () -> {
            restaurantService.create(restaurantRequestDto);
        });
    }

    //find all tests
    @Test
    void findAll_Success() {
        //given
        List<Restaurant> restaurants = Arrays.asList(restaurant);
        List<RestaurantResponseDto> responseDtos = Arrays.asList(restaurantResponseDto);
        when(restaurantRepository.findAll()).thenReturn(restaurants);
        when(restaurantMapper.toListDto(restaurants)).thenReturn(responseDtos);
        //when
        List<RestaurantResponseDto> result = restaurantService.findAll();
        //then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(responseDtos, result);
    }

    @Test
    void findAll_EmptyList() {
        // Given
        List<Restaurant> emptyRestaurants = List.of();
        List<RestaurantResponseDto> emptyDtos = List.of();
        when(restaurantRepository.findAll()).thenReturn(emptyRestaurants);
        when(restaurantMapper.toListDto(emptyRestaurants)).thenReturn(emptyDtos);
        // When
        List<RestaurantResponseDto> result = restaurantService.findAll();
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    //findById tests
    @Test
    void findById_Success() {
        //given
        when(restaurantRepository.findById(2)).thenReturn(Optional.of(restaurant));
        when(restaurantMapper.toDto(restaurant)).thenReturn(restaurantResponseDto);
        //when
        RestaurantResponseDto result = restaurantService.findById(2);
        //then
        assertNotNull(result);
        assertEquals(restaurantResponseDto, result);
    }

    @Test
    void findById_RestaurantNotFound() {
        //given
        when(restaurantRepository.findById(anyInt())).thenReturn(Optional.empty());
        //when&then
        RestaurantNotFoundException exception = assertThrows(RestaurantNotFoundException.class, () -> {
            restaurantService.findById(999);
        });
        assertEquals("Restaurant not found with ID 999", exception.getMessage());
    }

    //update Restaurant tests
    @Test
    void updateRestaurant_Success() {
        // Given
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);
        when(restaurantMapper.toDto(restaurant)).thenReturn(restaurantResponseDto);
        // When
        RestaurantResponseDto result = restaurantService.update(1, restaurantRequestDto);
        // Then
        assertNotNull(result);
        assertEquals(restaurantResponseDto, result);
        // interaction verification
        verify(restaurantRepository).findById(1);
        verify(restaurantRepository).save(restaurant);
        verify(restaurantMapper).toDto(restaurant);
    }

    @Test
    void updateRestaurant_RestaurantNotFound() {
        // Given
        when(restaurantRepository.findById(anyInt())).thenReturn(Optional.empty());
        // When & Then
        RestaurantNotFoundException exception = assertThrows(RestaurantNotFoundException.class, () -> {
            restaurantService.update(999, restaurantRequestDto);
        });
        assertEquals("Restaurant not found with ID 999", exception.getMessage());
    }

    @Test
    void updateRestaurant_RepositoryException() {
        // Given
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(restaurant)).thenThrow(new RuntimeException("Database error"));
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            restaurantService.update(1, restaurantRequestDto);
        });
    }

    //delete Restaurant tests
    @Test
    void deleteRestaurant_Success() {
        // Given
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        // When
        restaurantService.delete(1);
        // Then
        verify(restaurantRepository).findById(1);
        verify(restaurantRepository).delete(restaurant);
    }

    @Test
    void deleteRestaurant_RestaurantNotFound() {
        // Given
        when(restaurantRepository.findById(anyInt())).thenReturn(Optional.empty());
        // When & Then
        RestaurantNotFoundException exception = assertThrows(RestaurantNotFoundException.class, () -> {
            restaurantService.delete(999);
        });
        assertEquals("Restaurant not found with ID 999", exception.getMessage());
        verify(restaurantRepository).findById(999);
        verify(restaurantRepository, never()).delete(any());
    }

    @Test
    void deleteRestaurant_RepositoryException() {
        // Given
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        doThrow(new RuntimeException("Database error")).when(restaurantRepository).delete(restaurant);
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            restaurantService.delete(1);
        });
        verify(restaurantRepository).findById(1);
        verify(restaurantRepository).delete(restaurant);
    }

    //edge cases
    @Test
    void createRestaurant_NullInput_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> restaurantService.create(null));
    }
}