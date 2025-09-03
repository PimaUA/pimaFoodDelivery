package com.pimaua.core.service.restaurant;

import com.pimaua.core.dto.restaurant.MenuItemRequestDto;
import com.pimaua.core.dto.restaurant.MenuItemResponseDto;
import com.pimaua.core.entity.restaurant.Menu;
import com.pimaua.core.entity.restaurant.MenuItem;
import com.pimaua.core.entity.restaurant.Restaurant;
import com.pimaua.core.exception.custom.notfound.MenuItemNotFoundException;
import com.pimaua.core.mapper.restaurant.MenuItemMapper;
import com.pimaua.core.repository.restaurant.MenuItemRepository;
import com.pimaua.core.repository.restaurant.MenuRepository;
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
public class MenuItemServiceTest {
    @Mock
    private MenuItemRepository menuItemRepository;
    @Mock
    MenuRepository menuRepository;
    @Mock
    private MenuItemMapper menuItemMapper;

    @InjectMocks
    private MenuItemService menuItemService;

    private MenuItem menuItem;
    private MenuItemRequestDto menuItemRequestDto;
    private MenuItemResponseDto menuItemResponseDto;

    @BeforeEach
    void setup(){
        Restaurant restaurant = Restaurant.builder()
                .name("Some Restaurant")
                .address("Some Address")
                .build();

        Menu menu = Menu.builder()
                .name("Lunch Menu")
                .restaurant(restaurant)
                .build();

        menuItem =MenuItem.builder()
                .id(1)
                .name("Water")
                .description("Some water")
                .price(BigDecimal.valueOf(10.0))
                .isAvailable(true)
                .menu(menu)
                .updatedAt(LocalDateTime.now())
                .build();

        menuItemRequestDto =MenuItemRequestDto.builder()
                .name("Water")
                .description("Some water")
                .price(BigDecimal.valueOf(10.0))
                .isAvailable(true)
                .build();

        menuItemResponseDto =MenuItemResponseDto.builder()
                .id(1)
                .name("Water")
                .description("Some water")
                .price(BigDecimal.valueOf(10.0))
                .isAvailable(true)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createMenuItem_Success() {
        // given
        Integer menuId = menuItem.getMenu().getId(); // assuming menu has an id
        menuItemRequestDto.setMenuId(menuId);
        Menu menu = menuItem.getMenu(); // use the menu from setup
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));
        when(menuItemMapper.toEntity(menuItemRequestDto)).thenReturn(menuItem);
        when(menuItemRepository.save(menuItem)).thenReturn(menuItem);
        when(menuItemMapper.toDto(menuItem)).thenReturn(menuItemResponseDto);
        // when
        MenuItemResponseDto result = menuItemService.create(menuId,menuItemRequestDto);
        // then
        assertNotNull(result);
        assertEquals(menuItemResponseDto.getId(), result.getId());
        assertEquals(menuItemResponseDto.getName(), result.getName());
        // interaction verification
        verify(menuRepository).findById(menuId);
        verify(menuItemMapper).toEntity(menuItemRequestDto);
        verify(menuItemRepository).save(menuItem);
        verify(menuItemMapper).toDto(menuItem);
        // check that menu is correctly set
        assertEquals(menu, menuItem.getMenu(), "Menu should be set on MenuItem");
    }

    @Test
    void createMenuItem_RepositoryException() {
        // given
        Integer menuId = menuItem.getMenu().getId();
        menuItemRequestDto.setMenuId(menuId);
        Menu menu = menuItem.getMenu();
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));
        when(menuItemMapper.toEntity(menuItemRequestDto)).thenReturn(menuItem);
        when(menuItemRepository.save(menuItem)).thenThrow(new RuntimeException("Database error"));
        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            menuItemService.create(menuId,menuItemRequestDto);
        });
        assertEquals("Database error", exception.getMessage());
        // verify interactions
        verify(menuRepository).findById(menuId);
        verify(menuItemMapper).toEntity(menuItemRequestDto);
        verify(menuItemRepository).save(menuItem);
    }

    //find all tests
    @Test
    void findAll_Success() {
        //given
        List<MenuItem> menuItems = Arrays.asList(menuItem);
        List<MenuItemResponseDto> responseDtos = Arrays.asList(menuItemResponseDto);
        when(menuItemRepository.findAll()).thenReturn(menuItems);
        when(menuItemMapper.toPageDto(menuItems)).thenReturn(responseDtos);
        //when
        List<MenuItemResponseDto> result = menuItemService.findAllByMenuId();
        //then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(responseDtos, result);
    }

    @Test
    void findAll_EmptyList() {
        // Given
        List<MenuItem> emptyMenuItems = List.of();
        List<MenuItemResponseDto> emptyDtos = List.of();
        when(menuItemRepository.findAll()).thenReturn(emptyMenuItems);
        when(menuItemMapper.toPageDto(emptyMenuItems)).thenReturn(emptyDtos);
        // When
        List<MenuItemResponseDto> result = menuItemService.findAllByMenuId();
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    //findById tests
    @Test
    void findById_Success() {
        //given
        when(menuItemRepository.findById(2)).thenReturn(Optional.of(menuItem));
        when(menuItemMapper.toDto(menuItem)).thenReturn(menuItemResponseDto);
        //when
        MenuItemResponseDto result = menuItemService.findById(2);
        //then
        assertNotNull(result);
        assertEquals(menuItemResponseDto, result);
    }

    @Test
    void findById_MenuItemNotFound() {
        //given
        when(menuItemRepository.findById(anyInt())).thenReturn(Optional.empty());
        //when&then
        MenuItemNotFoundException exception = assertThrows(MenuItemNotFoundException.class, () -> {
            menuItemService.findById(999);
        });
        assertEquals("MenuItem not found with ID 999", exception.getMessage());
    }

    //update MenuItem tests
    @Test
    void updateMenuItem_Success() {
        // Given
        when(menuItemRepository.findById(1)).thenReturn(Optional.of(menuItem));
        when(menuItemRepository.save(menuItem)).thenReturn(menuItem);
        when(menuItemMapper.toDto(menuItem)).thenReturn(menuItemResponseDto);
        // When
        MenuItemResponseDto result = menuItemService.update(1, menuItemRequestDto);
        // Then
        assertNotNull(result);
        assertEquals(menuItemResponseDto, result);
        // interaction verification
        verify(menuItemRepository).findById(1);
        verify(menuItemRepository).save(menuItem);
        verify(menuItemMapper).toDto(menuItem);
    }

    @Test
    void updateMenuItem_MenuItemNotFound() {
        // Given
        when(menuItemRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then
        MenuItemNotFoundException exception = assertThrows(MenuItemNotFoundException.class, () -> {
            menuItemService.update(999, menuItemRequestDto);
        });
        assertEquals("MenuItem not found with ID 999", exception.getMessage());
    }

    @Test
    void updateMenuItem_RepositoryException() {
        // Given
        when(menuItemRepository.findById(1)).thenReturn(Optional.of(menuItem));
        when(menuItemRepository.save(menuItem)).thenThrow(new RuntimeException("Database error"));
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            menuItemService.update(1, menuItemRequestDto);
        });
    }

    //delete MenuItem tests
    @Test
    void deleteMenuItem_Success() {
        // Given
        when(menuItemRepository.findById(1)).thenReturn(Optional.of(menuItem));

        // When
        menuItemService.delete(1);

        // Then
        verify(menuItemRepository).findById(1);
        verify(menuItemRepository).delete(menuItem);
    }

    @Test
    void deleteMenuItem_MenuItemNotFound() {
        // Given
        when(menuItemRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then
        MenuItemNotFoundException exception = assertThrows(MenuItemNotFoundException.class, () -> {
            menuItemService.delete(999);
        });

        assertEquals("MenuItem not found with ID 999", exception.getMessage());
        verify(menuItemRepository).findById(999);
        verify(menuItemRepository, never()).delete(any());
    }

    @Test
    void deleteMenuItem_RepositoryException() {
        // Given
        when(menuItemRepository.findById(1)).thenReturn(Optional.of(menuItem));
        doThrow(new RuntimeException("Database error")).when(menuItemRepository).delete(menuItem);
        // When & Then
        assertThrows(RuntimeException.class, () -> {
            menuItemService.delete(1);
        });

        verify(menuItemRepository).findById(1);
        verify(menuItemRepository).delete(menuItem);
    }

    //edge cases
    @Test
    void createMenuItem_NullInput_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> menuItemService.create(1,null));
    }
}
