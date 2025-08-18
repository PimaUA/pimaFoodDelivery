package com.pimaua.core.service.restaurant;

import com.pimaua.core.dto.restaurant.MenuItemRequestDto;
import com.pimaua.core.dto.restaurant.MenuRequestDto;
import com.pimaua.core.dto.restaurant.MenuResponseDto;
import com.pimaua.core.entity.restaurant.Menu;
import com.pimaua.core.entity.restaurant.MenuItem;
import com.pimaua.core.entity.restaurant.Restaurant;
import com.pimaua.core.exception.custom.notfound.MenuNotFoundException;
import com.pimaua.core.mapper.restaurant.MenuMapper;
import com.pimaua.core.repository.restaurant.MenuRepository;
import com.pimaua.core.repository.restaurant.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuMapper menuMapper;
    @Mock
    RestaurantRepository restaurantRepository;

    @InjectMocks
    private MenuService menuService;

    private Menu menu;
    private MenuRequestDto menuRequestDto;
    private MenuResponseDto menuResponseDto;
    private MenuItem menuItem;
    private MenuItemRequestDto menuItemRequestDto;

    @BeforeEach
    void setup() {
        Restaurant restaurant = Restaurant.builder()
                .name("Some Restaurant")
                .address("Some Address")
                .build();

        menuItem = MenuItem.builder()
                .id(1)
                .name("Pizza")
                .description("Cheese Pizza")
                .price(BigDecimal.valueOf(9.99))
                .build();

        menuItemRequestDto = MenuItemRequestDto.builder()
                .name("Pizza")
                .description("Cheese Pizza")
                .price(BigDecimal.valueOf(9.99))
                .build();

        menu = Menu.builder()
                .id(1)
                .name("Some Restaurant")
                .isActive(true)
                .restaurant(restaurant)
                .menuItems(List.of(menuItem))
                .updatedAt(LocalDateTime.now())
                .build();

        menuRequestDto = MenuRequestDto.builder()
                .name("Some Restaurant")
                .isActive(true)
                .menuItems(List.of(menuItemRequestDto))
                .restaurantId(1)
                .build();

        menuResponseDto = MenuResponseDto.builder()
                .id(1)
                .name("Some Restaurant")
                .isActive(true)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    //CreateMenuTests
    @Test
    void createMenu_Success() {
        // given
        Restaurant restaurant = menu.getRestaurant();
        when(restaurantRepository.findById(anyInt())).thenReturn(Optional.of(restaurant));
        when(menuMapper.toEntity(menuRequestDto)).thenReturn(menu);
        when(menuRepository.save(menu)).thenReturn(menu);
        when(menuMapper.toDto(menu)).thenReturn(menuResponseDto);

        // when
        MenuResponseDto result = menuService.create(menuRequestDto);

        // then
        assertNotNull(result);
        assertEquals(menuResponseDto.getId(), result.getId());
        assertEquals(menuResponseDto.getName(), result.getName());

        // verify interactions
        verify(restaurantRepository).findById(anyInt());
        verify(menuMapper).toEntity(menuRequestDto);
        verify(menuRepository).save(menu);
        verify(menuMapper).toDto(menu);
    }

    @Test
    void createMenu_RepositoryException() {
        //given
        Restaurant restaurant = menu.getRestaurant(); // get a valid restaurant
        when(restaurantRepository.findById(any())).thenReturn(Optional.of(restaurant));
        when(menuMapper.toEntity(menuRequestDto)).thenReturn(menu);
        when(menuRepository.save(menu)).thenThrow(new RuntimeException("Database error"));
        //when&then
        assertThrows(RuntimeException.class, () -> {
            menuService.create(menuRequestDto);
        });
    }

    //find all tests
    @Test
    void findAll_Success() {
        //given
        List<Menu> menus = Arrays.asList(menu);
        List<MenuResponseDto> responseDtos = Arrays.asList(menuResponseDto);
        when(menuRepository.findAll()).thenReturn(menus);
        when(menuMapper.toListDto(menus)).thenReturn(responseDtos);
        //when
        List<MenuResponseDto> result = menuService.findAll();
        //then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(responseDtos, result);
    }

    @Test
    void findAll_EmptyList() {
        // Given
        List<Menu> emptyMenus = List.of();
        List<MenuResponseDto> emptyDtos = List.of();
        when(menuRepository.findAll()).thenReturn(emptyMenus);
        when(menuMapper.toListDto(emptyMenus)).thenReturn(emptyDtos);
        // When
        List<MenuResponseDto> result = menuService.findAll();
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    //findById tests
    @Test
    void findById_Success() {
        //given
        when(menuRepository.findById(2)).thenReturn(Optional.of(menu));
        when(menuMapper.toDto(menu)).thenReturn(menuResponseDto);
        //when
        MenuResponseDto result = menuService.findById(2);
        //then
        assertNotNull(result);
        assertEquals(menuResponseDto, result);
    }

    @Test
    void findById_MenuNotFound() {
        //given
        when(menuRepository.findById(anyInt())).thenReturn(Optional.empty());
        //when&then
        MenuNotFoundException exception = assertThrows(MenuNotFoundException.class, () -> {
            menuService.findById(999);
        });
        assertEquals("Menu not found with ID 999", exception.getMessage());
    }

    //update Menu tests
    @Test
    void updateMenu_Success() {
        // Given
        when(menuRepository.findById(1)).thenReturn(Optional.of(menu));
        when(menuRepository.save(menu)).thenReturn(menu);
        when(menuMapper.toDto(menu)).thenReturn(menuResponseDto);

        // When
        MenuResponseDto result = menuService.update(1, menuRequestDto);

        // Then
        assertNotNull(result);
        assertEquals(menuResponseDto, result);

        // interaction verification
        verify(menuRepository).findById(1);
        verify(menuRepository).save(menu);
        verify(menuMapper).toDto(menu);
    }

    @Test
    void updateMenu_MenuNotFound() {
        // Given
        when(menuRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then
        MenuNotFoundException exception = assertThrows(MenuNotFoundException.class, () -> {
            menuService.update(999, menuRequestDto);
        });
        assertEquals("Menu not found with ID 999", exception.getMessage());
    }

    @Test
    void updateMenu_RepositoryException() {
        // Given
        when(menuRepository.findById(1)).thenReturn(Optional.of(menu));
        when(menuRepository.save(menu)).thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            menuService.update(1, menuRequestDto);
        });
    }

    //delete Menu tests
    @Test
    void deleteMenu_Success() {
        // Given
        when(menuRepository.findById(1)).thenReturn(Optional.of(menu));

        // When
        menuService.delete(1);

        // Then
        verify(menuRepository).findById(1);
        verify(menuRepository).delete(menu);
    }

    @Test
    void deleteMenu_MenuNotFound() {
        // Given
        when(menuRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then
        MenuNotFoundException exception = assertThrows(MenuNotFoundException.class, () -> {
            menuService.delete(999);
        });

        assertEquals("Menu not found with ID 999", exception.getMessage());
        verify(menuRepository).findById(999);
        verify(menuRepository, never()).delete(any());
    }

    @Test
    void deleteMenu_RepositoryException() {
        // Given
        when(menuRepository.findById(1)).thenReturn(Optional.of(menu));
        doThrow(new RuntimeException("Database error")).when(menuRepository).delete(menu);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            menuService.delete(1);
        });

        verify(menuRepository).findById(1);
        verify(menuRepository).delete(menu);
    }

    //edge cases
    @Test
    void createMenu_NullInput_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> menuService.create(null));
    }
}
