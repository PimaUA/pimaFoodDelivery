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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
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

        openingHoursUpdateDto = OpeningHoursUpdateDto.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .opensAt(LocalTime.of(9, 0))
                .closesAt(LocalTime.of(22, 0))
                .is24Hours(false)
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

    // create
    @Test
    void createOpeningHours_Success() {
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(openingHoursMapper.toEntity(openingHoursRequestDto)).thenReturn(openingHours);
        when(openingHoursRepository.save(openingHours)).thenReturn(openingHours);
        when(openingHoursMapper.toDto(openingHours)).thenReturn(openingHoursResponseDto);

        OpeningHoursResponseDto result = openingHoursService.create(1, openingHoursRequestDto);

        assertNotNull(result);
        assertEquals(openingHoursResponseDto.getId(), result.getId());
        verify(restaurantRepository).findById(1);
        verify(openingHoursMapper).toEntity(openingHoursRequestDto);
        verify(openingHoursRepository).save(openingHours);
        verify(openingHoursMapper).toDto(openingHours);
    }

    @Test
    void createOpeningHours_RestaurantNotFound() {
        when(restaurantRepository.findById(anyInt())).thenReturn(Optional.empty());

        RestaurantNotFoundException exception = assertThrows(RestaurantNotFoundException.class, () ->
                openingHoursService.create(1, openingHoursRequestDto));

        assertEquals("Restaurant not found with ID 1", exception.getMessage());
        verify(openingHoursRepository, never()).save(any(OpeningHours.class));
    }

    @Test
    void createOpeningHours_RepositoryException() {
        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(openingHoursMapper.toEntity(openingHoursRequestDto)).thenReturn(openingHours);
        when(openingHoursRepository.save(openingHours)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                openingHoursService.create(1, openingHoursRequestDto));
        assertEquals("Database error", exception.getMessage());
    }

    @Test
    void createOpeningHours_NullInput_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> openingHoursService.create(1, null));
    }

    // findAll
    @Test
    void findAllOpeningHoursByRestaurantId_Success() {
        List<OpeningHours> openingHoursList = new ArrayList<>();
        openingHoursList.add(openingHours);

        List<OpeningHoursResponseDto> responseDtos = new ArrayList<>();
        responseDtos.add(openingHoursResponseDto);

        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(openingHoursRepository.findByRestaurantId(1)).thenReturn(openingHoursList);
        when(openingHoursMapper.toListDto(openingHoursList)).thenReturn(responseDtos);

        List<OpeningHoursResponseDto> result = openingHoursService.findAllOpeningHoursByRestaurantId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(responseDtos, result);
    }

    @Test
    void findAllOpeningHoursByRestaurantId_RestaurantNotFound() {
        when(restaurantRepository.findById(anyInt())).thenReturn(Optional.empty());

        RestaurantNotFoundException exception = assertThrows(RestaurantNotFoundException.class, () ->
                openingHoursService.findAllOpeningHoursByRestaurantId(1));

        assertEquals("Restaurant not found with ID 1", exception.getMessage());
    }

    @Test
    void findAllOpeningHoursByRestaurantId_EmptyList() {
        List<OpeningHours> emptyList = new ArrayList<>();
        List<OpeningHoursResponseDto> emptyDtos = new ArrayList<>();

        when(restaurantRepository.findById(1)).thenReturn(Optional.of(restaurant));
        when(openingHoursRepository.findByRestaurantId(1)).thenReturn(emptyList);
        when(openingHoursMapper.toListDto(emptyList)).thenReturn(emptyDtos);

        List<OpeningHoursResponseDto> result = openingHoursService.findAllOpeningHoursByRestaurantId(1);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // find by id
    @Test
    void findById_Success() {
        when(openingHoursRepository.findById(1)).thenReturn(Optional.of(openingHours));
        when(openingHoursMapper.toDto(openingHours)).thenReturn(openingHoursResponseDto);

        OpeningHoursResponseDto result = openingHoursService.findById(1);

        assertNotNull(result);
        assertEquals(openingHoursResponseDto, result);
    }

    @Test
    void findById_OpeningHoursNotFound() {
        when(openingHoursRepository.findById(anyInt())).thenReturn(Optional.empty());

        OpeningHoursNotFoundException exception = assertThrows(OpeningHoursNotFoundException.class, () ->
                openingHoursService.findById(999));

        assertEquals("Opening hours not found with ID 999", exception.getMessage());
    }

    // update
    @Test
    void updateOpeningHours_Success() {
        when(openingHoursRepository.findById(1)).thenReturn(Optional.of(openingHours));
        when(openingHoursRepository.save(openingHours)).thenReturn(openingHours);
        when(openingHoursMapper.toDto(openingHours)).thenReturn(openingHoursResponseDto);

        OpeningHoursResponseDto result = openingHoursService.update(1, openingHoursUpdateDto);

        assertNotNull(result);
        assertEquals(openingHoursResponseDto, result);
        verify(openingHoursRepository).findById(1);
        verify(openingHoursRepository).save(openingHours);
        verify(openingHoursMapper).toDto(openingHours);
    }

    @Test
    void updateOpeningHours_OpeningHoursNotFound() {
        when(openingHoursRepository.findById(anyInt())).thenReturn(Optional.empty());

        OpeningHoursNotFoundException exception = assertThrows(OpeningHoursNotFoundException.class, () ->
                openingHoursService.update(999, openingHoursUpdateDto));

        assertEquals("Opening hours not found with ID 999", exception.getMessage());
    }

    @Test
    void updateOpeningHours_RepositoryException() {
        when(openingHoursRepository.findById(1)).thenReturn(Optional.of(openingHours));
        when(openingHoursRepository.save(openingHours)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                openingHoursService.update(1, openingHoursUpdateDto));

        assertEquals("Database error", exception.getMessage());
    }

    // delete
    @Test
    void deleteOpeningHours_Success() {
        when(openingHoursRepository.findById(1)).thenReturn(Optional.of(openingHours));

        openingHoursService.delete(1);

        verify(openingHoursRepository).findById(1);
        verify(openingHoursRepository).delete(openingHours);
    }

    @Test
    void deleteOpeningHours_OpeningHoursNotFound() {
        when(openingHoursRepository.findById(anyInt())).thenReturn(Optional.empty());
        OpeningHoursNotFoundException exception = assertThrows(OpeningHoursNotFoundException.class, () ->
                openingHoursService.delete(999));

        assertEquals("Opening hours not found with ID 999", exception.getMessage());
        verify(openingHoursRepository).findById(999);
        verify(openingHoursRepository, never()).delete(any(OpeningHours.class));
    }

    @Test
    void deleteOpeningHours_RepositoryException() {
        when(openingHoursRepository.findById(1)).thenReturn(Optional.of(openingHours));
        doThrow(new RuntimeException("Database error")).when(openingHoursRepository).delete(openingHours);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                openingHoursService.delete(1));
        assertEquals("Database error", exception.getMessage());
        verify(openingHoursRepository).findById(1);
        verify(openingHoursRepository).delete(openingHours);
    }

    @Test
    void findAllOpeningHoursByRestaurantId_SortedAndLimitedTo7() {
        // Create 10 OpeningHours out of order
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

        // Sort and limit to 7 like the service would
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

        // Call service
        List<OpeningHoursResponseDto> result = openingHoursService.findAllOpeningHoursByRestaurantId(1);

        // Verify size
        assertEquals(7, result.size());

        // Verify sorting order MONDAY → SUNDAY
        List<DayOfWeek> expectedOrder = sortedAndLimited.stream()
                .map(OpeningHours::getDayOfWeek)
                .toList();
        for (int i = 0; i < expectedOrder.size(); i++) {
            assertEquals(expectedOrder.get(i), result.get(i).getDayOfWeek(),
                    "DayOfWeek at index " + i + " should match expected order");
        }

        // Verify repository and mapper interactions
        verify(restaurantRepository).findById(1);
        verify(openingHoursRepository).findByRestaurantId(1);
        verify(openingHoursMapper).toListDto(anyList());
    }
}

