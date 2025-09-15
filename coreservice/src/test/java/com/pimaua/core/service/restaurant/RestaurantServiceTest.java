package com.pimaua.core.service.restaurant;

import com.pimaua.core.dto.restaurant.RestaurantRequestDto;
import com.pimaua.core.dto.restaurant.RestaurantResponseDto;
import com.pimaua.core.entity.enums.DayOfWeek;
import com.pimaua.core.entity.restaurant.Menu;
import com.pimaua.core.entity.restaurant.OpeningHours;
import com.pimaua.core.entity.restaurant.Restaurant;
import com.pimaua.core.exception.ActiveMenuConflictException;
import com.pimaua.core.exception.custom.notfound.RestaurantNotFoundException;
import com.pimaua.core.mapper.restaurant.RestaurantMapper;
import com.pimaua.core.repository.restaurant.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
            List<Menu> mockMenus = List.of(
                    Menu.builder().id(1).name("Lunch Menu").isActive(true).build(),
                    Menu.builder().id(2).name("Dinner Menu").isActive(false).build()
            );

            List<OpeningHours> mockOpeningHours = List.of(
                    OpeningHours.builder().id(1).dayOfWeek(DayOfWeek.MONDAY)
                            .opensAt(LocalTime.of(9, 0)).closesAt(LocalTime.of(21, 0))
                            .is24Hours(false).build()
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

        // ----- CREATE -----
        @Test
        void createRestaurant_Success() {
            when(restaurantMapper.toEntity(restaurantRequestDto)).thenReturn(restaurant);
            when(restaurantRepository.save(restaurant)).thenReturn(restaurant);
            when(restaurantMapper.toDto(restaurant)).thenReturn(restaurantResponseDto);

            RestaurantResponseDto result = restaurantService.create(restaurantRequestDto);

            assertNotNull(result);
            assertEquals(restaurantResponseDto.getId(), result.getId());
            verify(restaurantMapper).toEntity(restaurantRequestDto);
            verify(restaurantRepository).save(restaurant);
            verify(restaurantMapper).toDto(restaurant);
        }

        @Test
        void createRestaurant_RepositoryException() {
            when(restaurantMapper.toEntity(restaurantRequestDto)).thenReturn(restaurant);
            when(restaurantRepository.save(restaurant)).thenThrow(new RuntimeException("Database error"));

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> restaurantService.create(restaurantRequestDto));
            assertEquals("Database error", exception.getMessage());
        }

        @Test
        void createRestaurant_NullInput_ThrowsIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class, () -> restaurantService.create(null));
        }

        //findAll
        @Test
        void findAll_Success() {
            List<Restaurant> restaurants = List.of(restaurant);
            List<RestaurantResponseDto> responseDtos = List.of(restaurantResponseDto);

            when(restaurantRepository.findAll()).thenReturn(restaurants);
            when(restaurantMapper.toListDto(restaurants)).thenReturn(responseDtos);

            List<RestaurantResponseDto> result = restaurantService.findAll();

            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(responseDtos, result);
        }

        @Test
        void findAll_EmptyList() {
            when(restaurantRepository.findAll()).thenReturn(List.of());
            when(restaurantMapper.toListDto(List.of())).thenReturn(List.of());

            List<RestaurantResponseDto> result = restaurantService.findAll();

            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        // findById
        @Test
        void findById_Success() {
            when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
            when(restaurantMapper.toDto(restaurant)).thenReturn(restaurantResponseDto);

            RestaurantResponseDto result = restaurantService.findById(1);

            assertNotNull(result);
            assertEquals(restaurantResponseDto, result);
        }

        @Test
        void findById_RestaurantNotFound() {
            when(restaurantRepository.findById(anyInt())).thenReturn(Optional.empty());

            RestaurantNotFoundException exception = assertThrows(RestaurantNotFoundException.class,
                    () -> restaurantService.findById(999));

            assertEquals("Restaurant not found with ID 999", exception.getMessage());
        }

        //update
        @Test
        void updateRestaurant_Success() {
            when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
            when(restaurantRepository.save(restaurant)).thenReturn(restaurant);
            when(restaurantMapper.toDto(restaurant)).thenReturn(restaurantResponseDto);

            RestaurantResponseDto result = restaurantService.update(1, restaurantRequestDto);

            assertNotNull(result);
            assertEquals(restaurantResponseDto, result);
            verify(restaurantRepository).findById(1);
            verify(restaurantRepository).save(restaurant);
            verify(restaurantMapper).toDto(restaurant);
        }

        @Test
        void updateRestaurant_RestaurantNotFound() {
            when(restaurantRepository.findById(anyInt())).thenReturn(Optional.empty());

            RestaurantNotFoundException exception = assertThrows(RestaurantNotFoundException.class,
                    () -> restaurantService.update(999, restaurantRequestDto));

            assertEquals("Restaurant not found with ID 999", exception.getMessage());
        }

        @Test
        void updateRestaurant_RepositoryException() {
            when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
            when(restaurantRepository.save(restaurant)).thenThrow(new RuntimeException("Database error"));

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> restaurantService.update(1, restaurantRequestDto));
            assertEquals("Database error", exception.getMessage());
        }

        //delete
        @Test
        void deleteRestaurant_Success_NoActiveMenus() {
            // Make all menus inactive
            restaurant.getMenus().forEach(menu -> menu.setIsActive(false));
            when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));

            restaurantService.delete(1);

            verify(restaurantRepository).findById(1);
            verify(restaurantRepository).delete(restaurant);
        }

        @Test
        void deleteRestaurant_FailsWithActiveMenus() {
            when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));

            ActiveMenuConflictException exception = assertThrows(ActiveMenuConflictException.class,
                    () -> restaurantService.delete(1));

            assertEquals("Cannot delete restaurant with active menus", exception.getMessage());
            verify(restaurantRepository).findById(1);
            verify(restaurantRepository, never()).delete(any(Restaurant.class));
        }

        @Test
        void deleteRestaurant_RestaurantNotFound() {
            when(restaurantRepository.findById(anyInt())).thenReturn(Optional.empty());

            RestaurantNotFoundException exception = assertThrows(RestaurantNotFoundException.class,
                    () -> restaurantService.delete(999));

            assertEquals("Restaurant not found with ID 999", exception.getMessage());
            verify(restaurantRepository).findById(999);
            verify(restaurantRepository, never()).delete(any(Restaurant.class));
        }

        @Test
        void deleteRestaurant_RepositoryException() {
            // Make all menus inactive
            restaurant.getMenus().forEach(menu -> menu.setIsActive(false));
            when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
            doThrow(new RuntimeException("Database error")).when(restaurantRepository).delete(restaurant);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> restaurantService.delete(1));
            assertEquals("Database error", exception.getMessage());

            verify(restaurantRepository).findById(1);
            verify(restaurantRepository).delete(restaurant);
        }
}