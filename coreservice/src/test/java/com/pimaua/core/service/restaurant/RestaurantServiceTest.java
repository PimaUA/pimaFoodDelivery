package com.pimaua.core.service.restaurant;

import com.pimaua.core.dto.restaurant.RestaurantRequestDto;
import com.pimaua.core.dto.restaurant.RestaurantResponseDto;
import com.pimaua.core.entity.restaurant.Restaurant;
import com.pimaua.core.exception.ActiveMenuConflictException;
import com.pimaua.core.exception.custom.notfound.RestaurantNotFoundException;
import com.pimaua.core.mapper.restaurant.RestaurantMapper;
import com.pimaua.core.repository.restaurant.RestaurantRepository;
import com.pimaua.core.service.restaurant.testdata.RestaurantTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
@Tag("unit")
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
        restaurant = RestaurantTestData.mockRestaurant();
        restaurantRequestDto = RestaurantTestData.mockRestaurantRequestDto();
        restaurantResponseDto = RestaurantTestData.mockRestaurantResponseDto();
    }

    // Create
    @Test
    void createRestaurant_Success() {
        // Given: a valid request DTO and mapper/repository stubs
        when(restaurantMapper.toEntity(restaurantRequestDto)).thenReturn(restaurant);
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);
        when(restaurantMapper.toDto(restaurant)).thenReturn(restaurantResponseDto);

        // When: creating a new restaurant
        RestaurantResponseDto result = restaurantService.create(restaurantRequestDto);

        // Then: the restaurant is created and mapped correctly
        assertNotNull(result);
        assertEquals(restaurantResponseDto.getId(), result.getId());
        verify(restaurantMapper).toEntity(restaurantRequestDto);
        verify(restaurantRepository).save(restaurant);
        verify(restaurantMapper).toDto(restaurant);
    }

    @Test
    void createRestaurant_RepositoryException() {
        // Given: repository throws a database error
        when(restaurantMapper.toEntity(restaurantRequestDto)).thenReturn(restaurant);
        when(restaurantRepository.save(restaurant)).thenThrow(new RuntimeException("Database error"));

        // When & Then: creating fails with RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> restaurantService.create(restaurantRequestDto));
        assertEquals("Database error", exception.getMessage());
    }

    @Test
    void createRestaurant_NullInput_ThrowsIllegalArgumentException() {
        // Given: null input DTO
        // When & Then: service throws IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> restaurantService.create(null));
    }

    //findAll
    @Test
    void findAll_Success() {
        // Given: repository returns one restaurant
        List<Restaurant> restaurants = List.of(restaurant);
        List<RestaurantResponseDto> responseDtos = List.of(restaurantResponseDto);
        when(restaurantRepository.findAll()).thenReturn(restaurants);
        when(restaurantMapper.toListDto(restaurants)).thenReturn(responseDtos);

        // When: fetching all restaurants
        List<RestaurantResponseDto> result = restaurantService.findAll();

        // Then: list with one restaurant DTO is returned
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(responseDtos, result);
    }

    @Test
    void findAll_EmptyList() {
        // Given: repository returns empty list
        when(restaurantRepository.findAll()).thenReturn(List.of());
        when(restaurantMapper.toListDto(List.of())).thenReturn(List.of());

        // When: fetching all restaurants
        List<RestaurantResponseDto> result = restaurantService.findAll();

        // Then: an empty list is returned
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // findById
    @Test
    void findById_Success() {
        // Given: repository finds a restaurant
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(restaurantMapper.toDto(restaurant)).thenReturn(restaurantResponseDto);

        // When: fetching by ID
        RestaurantResponseDto result = restaurantService.findById(1);

        // Then: the correct DTO is returned
        assertNotNull(result);
        assertEquals(restaurantResponseDto, result);
    }

    @Test
    void findById_RestaurantNotFound() {
        // Given: repository returns empty optional
        when(restaurantRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then: service throws RestaurantNotFoundException
        RestaurantNotFoundException exception = assertThrows(RestaurantNotFoundException.class,
                () -> restaurantService.findById(999));
        assertEquals("Restaurant not found with ID 999", exception.getMessage());
    }

    //update
    @Test
    void updateRestaurant_Success() {
        // Given: existing restaurant is found
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(restaurant)).thenReturn(restaurant);
        when(restaurantMapper.toDto(restaurant)).thenReturn(restaurantResponseDto);

        // When: updating restaurant
        RestaurantResponseDto result = restaurantService.update(1, restaurantRequestDto);

        // Then: updated restaurant is returned as DTO
        assertNotNull(result);
        assertEquals(restaurantResponseDto, result);
        verify(restaurantRepository).findById(1);
        verify(restaurantRepository).save(restaurant);
        verify(restaurantMapper).toDto(restaurant);
    }

    @Test
    void updateRestaurant_RestaurantNotFound() {
        // Given: repository returns empty optional
        when(restaurantRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then: update fails with RestaurantNotFoundException
        RestaurantNotFoundException exception = assertThrows(RestaurantNotFoundException.class,
                () -> restaurantService.update(999, restaurantRequestDto));
        assertEquals("Restaurant not found with ID 999", exception.getMessage());
    }

    @Test
    void updateRestaurant_RepositoryException() {
        // Given: repository throws database error on save
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(restaurantRepository.save(restaurant)).thenThrow(new RuntimeException("Database error"));

        // When & Then: update fails with RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> restaurantService.update(1, restaurantRequestDto));
        assertEquals("Database error", exception.getMessage());
    }

    //delete
    @Test
    void deleteRestaurant_Success_NoActiveMenus() {
        // Given: restaurant has no active menus
        restaurant.getMenus().forEach(menu -> menu.setIsActive(false));
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));

        // When: deleting restaurant
        restaurantService.delete(1);

        // Then: repository delete is called
        verify(restaurantRepository).findById(1);
        verify(restaurantRepository).delete(restaurant);
    }

    @Test
    void deleteRestaurant_FailsWithActiveMenus() {
        // Then: repository delete is called
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));

        // When & Then: deletion fails with ActiveMenuConflictException
        ActiveMenuConflictException exception = assertThrows(ActiveMenuConflictException.class,
                () -> restaurantService.delete(1));
        assertEquals("Cannot delete restaurant with active menus", exception.getMessage());
        verify(restaurantRepository).findById(1);
        verify(restaurantRepository, never()).delete(any(Restaurant.class));
    }

    @Test
    void deleteRestaurant_RestaurantNotFound() {
        // Given: repository returns empty optional
        when(restaurantRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then: deletion fails with RestaurantNotFoundException
        RestaurantNotFoundException exception = assertThrows(RestaurantNotFoundException.class,
                () -> restaurantService.delete(999));
        assertEquals("Restaurant not found with ID 999", exception.getMessage());
        verify(restaurantRepository).findById(999);
        verify(restaurantRepository, never()).delete(any(Restaurant.class));
    }

    @Test
    void deleteRestaurant_RepositoryException() {
        // Given: restaurant has no active menus but repository throws error
        restaurant.getMenus().forEach(menu -> menu.setIsActive(false));
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        doThrow(new RuntimeException("Database error")).when(restaurantRepository).delete(restaurant);

        // When & Then: deletion fails with RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> restaurantService.delete(1));
        assertEquals("Database error", exception.getMessage());
        verify(restaurantRepository).findById(1);
        verify(restaurantRepository).delete(restaurant);
    }
}