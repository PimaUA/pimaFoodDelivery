package com.pimaua.core.service.restaurant;

import com.pimaua.core.dto.restaurant.MenuItemRequestDto;
import com.pimaua.core.dto.restaurant.MenuRequestDto;
import com.pimaua.core.dto.restaurant.MenuResponseDto;
import com.pimaua.core.entity.restaurant.Menu;
import com.pimaua.core.entity.restaurant.MenuItem;
import com.pimaua.core.entity.restaurant.Restaurant;
import com.pimaua.core.exception.custom.notfound.MenuItemNotFoundException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        MenuResponseDto result = menuService.create(1,menuRequestDto);
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
    void createMenu_RestaurantNotFound_ThrowsException() {
        when(restaurantRepository.findById(anyInt())).thenReturn(Optional.empty());
        MenuItemNotFoundException exception = assertThrows(MenuItemNotFoundException.class, () -> {
            menuService.create(1, menuRequestDto);
        });
        assertEquals("Restaurant not found with ID 1", exception.getMessage());
        verify(restaurantRepository).findById(1);
        verify(menuRepository, never()).save(any());
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
            menuService.create(1,menuRequestDto);
        });
    }

    @Test
    void findAll_Success() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Menu> menus = new PageImpl<>(List.of(menu), pageable, 1);
        Page<MenuResponseDto> responseDtos = new PageImpl<>(List.of(menuResponseDto), pageable, 1);
        when(menuRepository.findByRestaurantIdAndIsActiveTrue(1, pageable)).thenReturn(menus);
        when(menuMapper.toPageDto(menus)).thenReturn(responseDtos);
        // when
        Page<MenuResponseDto> result = menuService.findAllByRestaurantId(1, true, pageable);
        // then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(responseDtos, result);
    }

    @Test
    void findAll_NoResults_ThrowsException() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Menu> emptyMenus = Page.empty(pageable);
        when(menuRepository.findByRestaurantIdAndIsActiveTrue(1, pageable)).thenReturn(emptyMenus);
        // when & then
        assertThrows(MenuNotFoundException.class,
                () -> menuService.findAllByRestaurantId(1, true, pageable));
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
        assertThrows(IllegalArgumentException.class, () -> menuService.create(1,null));
    }

    @Test
    void createMenu_AssignsIdToNewItems() {
        // Arrange: item with null ID
        MenuItem newItem = MenuItem.builder()
                .id(null)
                .name("Burger")
                .description("Beef Burger")
                .price(BigDecimal.valueOf(5.99))
                .build();
        Menu menuWithoutIds = Menu.builder()
                .id(2)
                .name("Menu with new item")
                .restaurant(menu.getRestaurant())
                .menuItems(List.of(newItem))
                .build();

        MenuResponseDto dto = MenuResponseDto.builder()
                .id(2)
                .name("Menu with new item")
                .build();
        when(restaurantRepository.findById(anyInt())).thenReturn(Optional.of(menu.getRestaurant()));
        when(menuMapper.toEntity(menuRequestDto)).thenReturn(menuWithoutIds);
        when(menuRepository.save(any(Menu.class))).thenAnswer(inv -> inv.getArgument(0));
        when(menuMapper.toDto(any(Menu.class))).thenReturn(dto);
        // Act
        MenuResponseDto result = menuService.create(1, menuRequestDto);
        // Assert
        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertNotNull(menuWithoutIds.getMenuItems().get(0).getId(), "New item should have generated ID");
    }

    @Test
    void updateMenu_PreservesAndGeneratesItemIds() {
        // Arrange: one item has ID, one doesn’t
        MenuItem existingItem = MenuItem.builder()
                .id(10)
                .name("Pizza")
                .price(BigDecimal.valueOf(9.99))
                .build();
        MenuItem newItem = MenuItem.builder()
                .id(null)
                .name("Salad")
                .price(BigDecimal.valueOf(4.99))
                .build();
        menu.setMenuItems(List.of(existingItem, newItem));
        when(menuRepository.findById(1)).thenReturn(Optional.of(menu));
        when(menuRepository.save(menu)).thenReturn(menu);
        when(menuMapper.toDto(menu)).thenReturn(menuResponseDto);
        // Act
        MenuResponseDto result = menuService.update(1, menuRequestDto);
        // Assert
        assertNotNull(result);
        assertNotNull(newItem.getId(), "New item should get a generated ID");
        assertEquals(10, existingItem.getId(), "Existing item ID should be preserved");
        assertNotNull(existingItem.getUpdatedAt(), "UpdatedAt should be refreshed");
    }
}
