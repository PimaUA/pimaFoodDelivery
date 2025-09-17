package com.pimaua.core.service.restaurant;

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
import com.pimaua.core.service.restaurant.testdata.MenuTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
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
@Tag("unit")
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

    @BeforeEach
    void setup() {
        menu = MenuTestData.mockMenu();
        menuRequestDto = MenuTestData.mockMenuRequestDto();
        menuResponseDto = MenuTestData.mockMenuResponseDto();
    }

    //CreateMenuTests
    @Test
    void createMenu_Success() {
        // Given: a restaurant exists and a valid menu request
        Restaurant restaurant = menu.getRestaurant();
        when(restaurantRepository.findById(anyInt())).thenReturn(Optional.of(restaurant));
        when(menuMapper.toEntity(menuRequestDto)).thenReturn(menu);
        when(menuRepository.save(menu)).thenReturn(menu);
        when(menuMapper.toDto(menu)).thenReturn(menuResponseDto);

        // When: creating the menu
        MenuResponseDto result = menuService.create(1, menuRequestDto);

        // Then: result should not be null and fields should match expected
        assertNotNull(result);
        assertEquals(menuResponseDto.getId(), result.getId());
        assertEquals(menuResponseDto.getName(), result.getName());

        // Verify interactions with mocks
        verify(restaurantRepository).findById(anyInt());
        verify(menuMapper).toEntity(menuRequestDto);
        verify(menuRepository).save(menu);
        verify(menuMapper).toDto(menu);
    }

    @Test
    void createMenu_RestaurantNotFound_ThrowsException() {
        // Given: no restaurant found
        when(restaurantRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then: creating menu throws exception
        MenuItemNotFoundException exception = assertThrows(MenuItemNotFoundException.class, () -> {
            menuService.create(1, menuRequestDto);
        });
        assertEquals("Restaurant not found with ID 1", exception.getMessage());
        verify(restaurantRepository).findById(1);
        verify(menuRepository, never()).save(any());
    }

    @Test
    void createMenu_RepositoryException() {
        // Given: repository will throw an exception on save
        Restaurant restaurant = menu.getRestaurant(); // get a valid restaurant
        when(restaurantRepository.findById(any())).thenReturn(Optional.of(restaurant));
        when(menuMapper.toEntity(menuRequestDto)).thenReturn(menu);
        when(menuRepository.save(menu)).thenThrow(new RuntimeException("Database error"));

        // When & Then: creating menu throws RuntimeException
        assertThrows(RuntimeException.class, () -> {
            menuService.create(1, menuRequestDto);
        });
    }

    @Test
    void findAll_Success() {
        // Given: menu repository returns a page of menus
        Pageable pageable = PageRequest.of(0, 10);
        Page<Menu> menus = new PageImpl<>(List.of(menu), pageable, 1);
        Page<MenuResponseDto> responseDtos = new PageImpl<>(List.of(menuResponseDto), pageable, 1);
        when(menuRepository.findByRestaurantIdAndIsActiveTrue(1, pageable)).thenReturn(menus);
        when(menuMapper.toPageDto(menus)).thenReturn(responseDtos);

        // When: fetching all menus
        Page<MenuResponseDto> result = menuService.findAllByRestaurantId(1, true, pageable);

        // Then: result matches expected
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(responseDtos, result);
    }

    @Test
    void findAll_NoResults_ThrowsException() {
        // Given: repository returns empty page
        Pageable pageable = PageRequest.of(0, 10);
        Page<Menu> emptyMenus = Page.empty(pageable);
        when(menuRepository.findByRestaurantIdAndIsActiveTrue(1, pageable)).thenReturn(emptyMenus);

        // When & Then: service throws exception
        assertThrows(MenuNotFoundException.class,
                () -> menuService.findAllByRestaurantId(1, true, pageable));
    }

    //findById tests
    @Test
    void findById_Success() {
        // Given: a menu exists with ID 2
        when(menuRepository.findById(2)).thenReturn(Optional.of(menu));
        when(menuMapper.toDto(menu)).thenReturn(menuResponseDto);

        // When: fetching by ID
        MenuResponseDto result = menuService.findById(2);

        // Then: result matches expected
        assertNotNull(result);
        assertEquals(menuResponseDto, result);
    }

    @Test
    void findById_MenuNotFound() {
        // Given: no menu exists
        when(menuRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then: fetching non-existent menu throws exception
        MenuNotFoundException exception = assertThrows(MenuNotFoundException.class, () -> {
            menuService.findById(999);
        });
        assertEquals("Menu not found with ID 999", exception.getMessage());
    }

    //update Menu tests
    @Test
    void updateMenu_Success() {
        // Given: menu exists and can be updated
        when(menuRepository.findById(1)).thenReturn(Optional.of(menu));
        when(menuRepository.save(menu)).thenReturn(menu);
        when(menuMapper.toDto(menu)).thenReturn(menuResponseDto);

        // When: updating menu
        MenuResponseDto result = menuService.update(1, menuRequestDto);

        // Then: result matches expected
        assertNotNull(result);
        assertEquals(menuResponseDto, result);

        // interaction verification
        verify(menuRepository).findById(1);
        verify(menuRepository).save(menu);
        verify(menuMapper).toDto(menu);
    }

    @Test
    void updateMenu_MenuNotFound() {
        // Given: menu not found
        when(menuRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then: updating throws exception
        MenuNotFoundException exception = assertThrows(MenuNotFoundException.class, () -> {
            menuService.update(999, menuRequestDto);
        });
        assertEquals("Menu not found with ID 999", exception.getMessage());
    }

    @Test
    void updateMenu_RepositoryException() {
        // Given: repository throws exception on save
        when(menuRepository.findById(1)).thenReturn(Optional.of(menu));
        when(menuRepository.save(menu)).thenThrow(new RuntimeException("Database error"));

        // When & Then: update throws RuntimeException
        assertThrows(RuntimeException.class, () -> {
            menuService.update(1, menuRequestDto);
        });
    }

    //delete Menu tests
    @Test
    void deleteMenu_Success() {
        // Given: menu exists
        when(menuRepository.findById(1)).thenReturn(Optional.of(menu));

        // When: deleting menu
        menuService.delete(1);

        // Then: verify interactions
        verify(menuRepository).findById(1);
        verify(menuRepository).delete(menu);
    }

    @Test
    void deleteMenu_MenuNotFound() {
        // Given: menu does not exist
        when(menuRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then: deleting non-existent menu throws exception
        MenuNotFoundException exception = assertThrows(MenuNotFoundException.class, () -> {
            menuService.delete(999);
        });
        assertEquals("Menu not found with ID 999", exception.getMessage());
        verify(menuRepository).findById(999);
        verify(menuRepository, never()).delete(any());
    }

    @Test
    void deleteMenu_RepositoryException() {
        // Given: delete throws exception
        when(menuRepository.findById(1)).thenReturn(Optional.of(menu));
        doThrow(new RuntimeException("Database error")).when(menuRepository).delete(menu);

        // When & Then: delete throws RuntimeException
        assertThrows(RuntimeException.class, () -> {
            menuService.delete(1);
        });
        verify(menuRepository).findById(1);
        verify(menuRepository).delete(menu);
    }

    //edge cases
    @Test
    void createMenu_NullInput_ThrowsIllegalArgumentException() {
        // When & Then: creating menu with null input throws exception
        assertThrows(IllegalArgumentException.class, () -> menuService.create(1, null));
    }

    @Test
    void createMenu_AssignsIdToNewItems() {
        // Given: menu item without ID
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

        // When: creating menu
        MenuResponseDto result = menuService.create(1, menuRequestDto);

        // Then: menu item should get generated ID
        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertNotNull(menuWithoutIds.getMenuItems().get(0).getId(), "New item should have generated ID");
    }

    @Test
    void updateMenu_PreservesAndGeneratesItemIds() {
        // Given: menu has one existing item with ID and one new item without ID
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

        // When: updating menu
        MenuResponseDto result = menuService.update(1, menuRequestDto);

        // Then: new item gets ID, existing item preserves ID, updatedAt is refreshed
        assertNotNull(result);
        assertNotNull(newItem.getId(), "New item should get a generated ID");
        assertEquals(10, existingItem.getId(), "Existing item ID should be preserved");
        assertNotNull(existingItem.getUpdatedAt(), "UpdatedAt should be refreshed");
    }
}
