package com.pimaua.core.service.restaurant;

import com.pimaua.core.dto.restaurant.OpeningHoursRequestDto;
import com.pimaua.core.dto.restaurant.OpeningHoursResponseDto;
import com.pimaua.core.dto.restaurant.OpeningHoursUpdateDto;
import com.pimaua.core.entity.enums.DayOfWeek;
import com.pimaua.core.entity.restaurant.OpeningHours;
import com.pimaua.core.entity.restaurant.Restaurant;
import com.pimaua.core.exception.custom.notfound.OpeningHoursNotFoundException;
import com.pimaua.core.exception.custom.notfound.RestaurantNotFoundException;
import com.pimaua.core.mapper.restaurant.OpeningHoursMapper;
import com.pimaua.core.repository.restaurant.OpeningHoursRepository;
import com.pimaua.core.repository.restaurant.RestaurantRepository;
import com.pimaua.core.service.restaurant.testdata.OpeningHoursTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Comparator;
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
    private OpeningHoursUpdateDto openingHoursUpdateDto;
    private OpeningHoursResponseDto openingHoursResponseDto;
    private Restaurant restaurant;

    @BeforeEach
    void setup() {
        restaurant = OpeningHoursTestData.mockRestaurant();
        openingHours = OpeningHoursTestData.mockOpeningHours(restaurant);
        openingHoursRequestDto = OpeningHoursTestData.mockOpeningHoursRequestDto(restaurant);
        openingHoursUpdateDto = OpeningHoursTestData.mockOpeningHoursUpdateDto();
        openingHoursResponseDto = OpeningHoursTestData.mockOpeningHoursResponseDto();
    }

    // create
    @Test
    void createOpeningHours_Success() {
        // Given: a restaurant exists and mapper works correctly
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(openingHoursMapper.toEntity(openingHoursRequestDto)).thenReturn(openingHours);
        when(openingHoursRepository.save(openingHours)).thenReturn(openingHours);
        when(openingHoursMapper.toDto(openingHours)).thenReturn(openingHoursResponseDto);

        // When: creating opening hours
        OpeningHoursResponseDto result = openingHoursService.create(1, openingHoursRequestDto);

        // Then: result matches expected and interactions are verified
        assertNotNull(result);
        assertEquals(openingHoursResponseDto.getId(), result.getId());
        verify(restaurantRepository).findById(1);
        verify(openingHoursMapper).toEntity(openingHoursRequestDto);
        verify(openingHoursRepository).save(openingHours);
        verify(openingHoursMapper).toDto(openingHours);
    }

    @Test
    void createOpeningHours_RestaurantNotFound() {
        // Given: restaurant is missing
        when(restaurantRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then: service throws exception
        RestaurantNotFoundException exception = assertThrows(RestaurantNotFoundException.class, () ->
                openingHoursService.create(1, openingHoursRequestDto));
        assertEquals("Restaurant not found with ID 1", exception.getMessage());
        verify(openingHoursRepository, never()).save(any(OpeningHours.class));
    }

    @Test
    void createOpeningHours_RepositoryException() {
        // Given: repository fails when saving
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(openingHoursMapper.toEntity(openingHoursRequestDto)).thenReturn(openingHours);
        when(openingHoursRepository.save(openingHours)).thenThrow(new RuntimeException("Database error"));

        // When & Then: service throws runtime exception
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                openingHoursService.create(1, openingHoursRequestDto));
        assertEquals("Database error", exception.getMessage());
    }

    @Test
    void createOpeningHours_NullInput_ThrowsIllegalArgumentException() {
        // When & Then: null input throws IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> openingHoursService.create(1, null));
    }

    // findAll
    @Test
    void findAllOpeningHoursByRestaurantId_Success() {
        // Given: restaurant exists and repository returns opening hours
        List<OpeningHours> openingHoursList = new ArrayList<>();
        openingHoursList.add(openingHours);

        List<OpeningHoursResponseDto> responseDtos = new ArrayList<>();
        responseDtos.add(openingHoursResponseDto);

        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(openingHoursRepository.findByRestaurantId(1)).thenReturn(openingHoursList);
        when(openingHoursMapper.toListDto(openingHoursList)).thenReturn(responseDtos);

        // When: fetching all opening hours
        List<OpeningHoursResponseDto> result = openingHoursService.findAllOpeningHoursByRestaurantId(1);

        // Then: result matches expected
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(responseDtos, result);
    }

    @Test
    void findAllOpeningHoursByRestaurantId_RestaurantNotFound() {
        // Then: result matches expected
        when(restaurantRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then: service throws exception
        RestaurantNotFoundException exception = assertThrows(RestaurantNotFoundException.class, () ->
                openingHoursService.findAllOpeningHoursByRestaurantId(1));
        assertEquals("Restaurant not found with ID 1", exception.getMessage());
    }

    @Test
    void findAllOpeningHoursByRestaurantId_EmptyList() {
        // Given: restaurant exists but repository returns empty list
        List<OpeningHours> emptyList = new ArrayList<>();
        List<OpeningHoursResponseDto> emptyDtos = new ArrayList<>();

        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(openingHoursRepository.findByRestaurantId(1)).thenReturn(emptyList);
        when(openingHoursMapper.toListDto(emptyList)).thenReturn(emptyDtos);

        // When: fetching opening hours
        List<OpeningHoursResponseDto> result = openingHoursService.findAllOpeningHoursByRestaurantId(1);

        // Then: result is an empty list
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // find by id
    @Test
    void findById_Success() {
        // Given: opening hours exist
        when(openingHoursRepository.findById(1)).thenReturn(Optional.of(openingHours));
        when(openingHoursMapper.toDto(openingHours)).thenReturn(openingHoursResponseDto);

        // When: fetching by ID
        OpeningHoursResponseDto result = openingHoursService.findById(1);

        // Then: result matches expected
        assertNotNull(result);
        assertEquals(openingHoursResponseDto, result);
    }

    @Test
    void findById_OpeningHoursNotFound() {
        // Given: no opening hours found
        when(openingHoursRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then: service throws exception
        OpeningHoursNotFoundException exception = assertThrows(OpeningHoursNotFoundException.class, () ->
                openingHoursService.findById(999));
        assertEquals("Opening hours not found with ID 999", exception.getMessage());
    }

    // update
    @Test
    void updateOpeningHours_Success() {
        // Given: opening hours exist and repository updates successfully
        when(openingHoursRepository.findById(1)).thenReturn(Optional.of(openingHours));
        when(openingHoursRepository.save(openingHours)).thenReturn(openingHours);
        when(openingHoursMapper.toDto(openingHours)).thenReturn(openingHoursResponseDto);

        // When: updating opening hours
        OpeningHoursResponseDto result = openingHoursService.update(1, openingHoursUpdateDto);

        // Then: result matches expected and interactions verified
        assertNotNull(result);
        assertEquals(openingHoursResponseDto, result);
        verify(openingHoursRepository).findById(1);
        verify(openingHoursRepository).save(openingHours);
        verify(openingHoursMapper).toDto(openingHours);
    }

    @Test
    void updateOpeningHours_OpeningHoursNotFound() {
        // Given: opening hours not found
        when(openingHoursRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then: update throws exception
        OpeningHoursNotFoundException exception = assertThrows(OpeningHoursNotFoundException.class, () ->
                openingHoursService.update(999, openingHoursUpdateDto));
        assertEquals("Opening hours not found with ID 999", exception.getMessage());
    }

    @Test
    void updateOpeningHours_RepositoryException() {
        // Given: repository throws exception on save
        when(openingHoursRepository.findById(1)).thenReturn(Optional.of(openingHours));
        when(openingHoursRepository.save(openingHours)).thenThrow(new RuntimeException("Database error"));

        // When & Then: update throws RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                openingHoursService.update(1, openingHoursUpdateDto));
        assertEquals("Database error", exception.getMessage());
    }

    // delete
    @Test
    void deleteOpeningHours_Success() {
        // Given: opening hours exist
        when(openingHoursRepository.findById(1)).thenReturn(Optional.of(openingHours));

        // When: deleting opening hours
        openingHoursService.delete(1);

        // Then: interactions verified
        verify(openingHoursRepository).findById(1);
        verify(openingHoursRepository).delete(openingHours);
    }

    @Test
    void deleteOpeningHours_OpeningHoursNotFound() {
        // Given: no opening hours found
        when(openingHoursRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then: delete throws exception
        OpeningHoursNotFoundException exception = assertThrows(OpeningHoursNotFoundException.class, () ->
                openingHoursService.delete(999));
        assertEquals("Opening hours not found with ID 999", exception.getMessage());
        verify(openingHoursRepository).findById(999);
        verify(openingHoursRepository, never()).delete(any(OpeningHours.class));
    }

    @Test
    void deleteOpeningHours_RepositoryException() {
        // Given: repository throws exception on delete
        when(openingHoursRepository.findById(1)).thenReturn(Optional.of(openingHours));
        doThrow(new RuntimeException("Database error")).when(openingHoursRepository).delete(openingHours);

        // When & Then: delete throws RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                openingHoursService.delete(1));
        assertEquals("Database error", exception.getMessage());
        verify(openingHoursRepository).findById(1);
        verify(openingHoursRepository).delete(openingHours);
    }

    @Test
    void findAllOpeningHoursByRestaurantId_SortedAndLimitedTo7() {
        // Given: repository returns 10 opening hours out of order
        List<OpeningHours> openingHoursList = new ArrayList<>(List.of(
                OpeningHours.builder().dayOfWeek(DayOfWeek.WEDNESDAY).restaurant(restaurant).build(),
                OpeningHours.builder().dayOfWeek(DayOfWeek.MONDAY).restaurant(restaurant).build(),
                OpeningHours.builder().dayOfWeek(DayOfWeek.FRIDAY).restaurant(restaurant).build(),
                OpeningHours.builder().dayOfWeek(DayOfWeek.SUNDAY).restaurant(restaurant).build(),
                OpeningHours.builder().dayOfWeek(DayOfWeek.TUESDAY).restaurant(restaurant).build(),
                OpeningHours.builder().dayOfWeek(DayOfWeek.THURSDAY).restaurant(restaurant).build(),
                OpeningHours.builder().dayOfWeek(DayOfWeek.SATURDAY).restaurant(restaurant).build(),
                OpeningHours.builder().dayOfWeek(DayOfWeek.MONDAY).restaurant(restaurant).build(),
                OpeningHours.builder().dayOfWeek(DayOfWeek.FRIDAY).restaurant(restaurant).build(),
                OpeningHours.builder().dayOfWeek(DayOfWeek.SUNDAY).restaurant(restaurant).build()
        ));

        // And: expected result (sorted MONDAY → SUNDAY, limited to 7)
        List<OpeningHours> sortedAndLimited = new ArrayList<>(openingHoursList);
        sortedAndLimited.sort(Comparator.comparing(oh -> oh.getDayOfWeek().getValue()));
        sortedAndLimited = sortedAndLimited.stream().limit(7).toList();

        // Map to DTOs
        List<OpeningHoursResponseDto> responseDtos = sortedAndLimited.stream()
                .map(oh -> OpeningHoursResponseDto.builder()
                        .dayOfWeek(oh.getDayOfWeek())
                        .build())
                .toList();

        // Mock repository and mapper
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(openingHoursRepository.findByRestaurantId(1)).thenReturn(openingHoursList);
        when(openingHoursMapper.toListDto(anyList())).thenReturn(responseDtos);

        // When: fetching opening hours
        List<OpeningHoursResponseDto> result = openingHoursService.findAllOpeningHoursByRestaurantId(1);

        // Then: only 7 remain, sorted MONDAY → SUNDAY
        assertEquals(7, result.size());
        List<DayOfWeek> expectedOrder = sortedAndLimited.stream()
                .map(OpeningHours::getDayOfWeek)
                .toList();
        for (int i = 0; i < expectedOrder.size(); i++) {
            assertEquals(expectedOrder.get(i), result.get(i).getDayOfWeek(),
                    "DayOfWeek at index " + i + " should match expected order");
        }

        // And: repository + mapper were called
        verify(restaurantRepository).findById(1);
        verify(openingHoursRepository).findByRestaurantId(1);
        verify(openingHoursMapper).toListDto(anyList());
    }
}

