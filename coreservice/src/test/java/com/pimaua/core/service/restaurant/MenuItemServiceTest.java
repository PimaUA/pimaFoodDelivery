package com.pimaua.core.service.restaurant;

import com.pimaua.core.dto.restaurant.MenuItemRequestDto;
import com.pimaua.core.dto.restaurant.MenuItemResponseDto;
import com.pimaua.core.entity.restaurant.Menu;
import com.pimaua.core.entity.restaurant.MenuItem;
import com.pimaua.core.exception.custom.notfound.MenuItemNotFoundException;
import com.pimaua.core.mapper.restaurant.MenuItemMapper;
import com.pimaua.core.repository.restaurant.MenuItemRepository;
import com.pimaua.core.repository.restaurant.MenuRepository;
import com.pimaua.core.service.restaurant.testdata.MenuItemTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

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
    void setup() {
        menuItem = MenuItemTestData.mockMenuItem();
        menuItemRequestDto = MenuItemTestData.mockMenuItemRequestDto();
        menuItemResponseDto = MenuItemTestData.mockMenuItemResponseDto();
    }

    @Test
    void createMenuItem_Success() {
        // Where / Given: a valid menu ID and MenuItemRequestDto
        Integer menuId = menuItem.getMenu().getId(); // assuming menu has an id
        Menu menu = menuItem.getMenu(); // use the menu from setup
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));
        when(menuItemMapper.toEntity(menuItemRequestDto)).thenReturn(menuItem);
        when(menuItemRepository.save(menuItem)).thenReturn(menuItem);
        when(menuItemMapper.toDto(menuItem)).thenReturn(menuItemResponseDto);

        // When: create is called
        MenuItemResponseDto result = menuItemService.create(menuId, menuItemRequestDto);

        // Then: verify result, mappings, and menu set
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
        // Where / Given: repository throws exception
        Integer menuId = menuItem.getMenu().getId();
        Menu menu = menuItem.getMenu();
        when(menuRepository.findById(menuId)).thenReturn(Optional.of(menu));
        when(menuItemMapper.toEntity(menuItemRequestDto)).thenReturn(menuItem);
        when(menuItemRepository.save(menuItem)).thenThrow(new RuntimeException("Database error"));

        // When & Then: exception is thrown
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            menuItemService.create(menuId, menuItemRequestDto);
        });
        assertEquals("Database error", exception.getMessage());

        // Then: verify interactions
        verify(menuRepository).findById(menuId);
        verify(menuItemMapper).toEntity(menuItemRequestDto);
        verify(menuItemRepository).save(menuItem);
    }

    @SuppressWarnings("unchecked")
    @Test
    void findAll_Success() {
        // Where / Given: repository returns one page of menu items
        Page<MenuItem> menuItemsPage = new PageImpl<>(List.of(menuItem));
        Page<MenuItemResponseDto> responseDtosPage = new PageImpl<>(List.of(menuItemResponseDto));
        when(menuItemRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(menuItemsPage);
        when(menuItemMapper.toPageDto(menuItemsPage)).thenReturn(responseDtosPage);

        // When: findAllByMenuId is called
        Page<MenuItemResponseDto> result =
                menuItemService.findAllByMenuId(1, Pageable.unpaged(), "Water", true);

        // Then: verify result and interactions
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(menuItemResponseDto, result.getContent().get(0));
        verify(menuItemRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(menuItemMapper).toPageDto(menuItemsPage);
    }

    @SuppressWarnings("unchecked")
    @Test
    void findAll_EmptyList() {
        // Where / Given: repository returns an empty page of MenuItems
        Page<MenuItem> emptyMenuItemsPage = Page.empty();
        Page<MenuItemResponseDto> emptyDtosPage = Page.empty();
        when(menuItemRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(emptyMenuItemsPage);
        when(menuItemMapper.toPageDto(emptyMenuItemsPage)).thenReturn(emptyDtosPage);

        // When: findAllByMenuId is called with no filters
        Page<MenuItemResponseDto> result =
                menuItemService.findAllByMenuId(1, Pageable.unpaged(), null, true);

        // Then: result is not null and empty, repository and mapper called
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(menuItemRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(menuItemMapper).toPageDto(emptyMenuItemsPage);
    }

    //findById tests
    @Test
    void findById_Success() {
        // Where / Given: menu item exists
        when(menuItemRepository.findById(2)).thenReturn(Optional.of(menuItem));
        when(menuItemMapper.toDto(menuItem)).thenReturn(menuItemResponseDto);

        // When: findById is called
        MenuItemResponseDto result = menuItemService.findById(2);

        // Then: result matches expected
        assertNotNull(result);
        assertEquals(menuItemResponseDto, result);
    }

    @Test
    void findById_MenuItemNotFound() {
        // Where / Given: menu item does not exist
        when(menuItemRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then: exception is thrown
        MenuItemNotFoundException exception = assertThrows(MenuItemNotFoundException.class, () -> {
            menuItemService.findById(999);
        });
        assertEquals("MenuItem not found with ID 999", exception.getMessage());
    }

    //update MenuItem tests
    @Test
    void updateMenuItem_Success() {
        // Where / Given: menu item exists and valid update DTO
        when(menuItemRepository.findById(1)).thenReturn(Optional.of(menuItem));
        when(menuItemRepository.save(menuItem)).thenReturn(menuItem);
        when(menuItemMapper.toDto(menuItem)).thenReturn(menuItemResponseDto);

        // When: update is called
        MenuItemResponseDto result = menuItemService.update(1, menuItemRequestDto);

        // Then: result is correct and repository/mapping called
        assertNotNull(result);
        assertEquals(menuItemResponseDto, result);
        verify(menuItemRepository).findById(1);
        verify(menuItemRepository).save(menuItem);
        verify(menuItemMapper).toDto(menuItem);
    }

    @Test
    void updateMenuItem_MenuItemNotFound() {
        // Where / Given: the repository does not find a MenuItem with the given ID
        when(menuItemRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When: update is called on a non-existent MenuItem
        // Then: MenuItemNotFoundException is thrown with the correct message
        MenuItemNotFoundException exception = assertThrows(MenuItemNotFoundException.class, () -> {
            menuItemService.update(999, menuItemRequestDto);
        });

        // Then: verify exception message
        assertEquals("MenuItem not found with ID 999", exception.getMessage());
    }

    @Test
    void updateMenuItem_RepositoryException() {
        // Where / Given: repository finds the MenuItem but throws an exception on save
        when(menuItemRepository.findById(1)).thenReturn(Optional.of(menuItem));
        when(menuItemRepository.save(menuItem)).thenThrow(new RuntimeException("Database error"));

        // When: update is called
        // Then: RuntimeException is thrown
        assertThrows(RuntimeException.class, () -> {
            menuItemService.update(1, menuItemRequestDto);
        });
    }

    //delete MenuItem tests
    @Test
    void deleteMenuItem_Success() {
        // Where / Given: menu item exists
        when(menuItemRepository.findById(1)).thenReturn(Optional.of(menuItem));

        // When: delete is called
        menuItemService.delete(1);

        // Then: repository delete is called
        verify(menuItemRepository).findById(1);
        verify(menuItemRepository).delete(menuItem);
    }

    @Test
    void deleteMenuItem_MenuItemNotFound() {
        // Where / Given: repository does not contain a MenuItem with the given ID
        when(menuItemRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        // When: delete is called with a non-existent MenuItem ID
        // Then: MenuItemNotFoundException is thrown
        MenuItemNotFoundException exception = assertThrows(MenuItemNotFoundException.class, () -> {
            menuItemService.delete(999);
        });

        // Then: exception message is verified and repository delete is never called
        assertEquals("MenuItem not found with ID 999", exception.getMessage());
        verify(menuItemRepository).findById(999);
        verify(menuItemRepository, never()).delete(any(MenuItem.class));
    }

    @Test
    void deleteMenuItem_RepositoryException() {
        // Where / Given: repository contains the MenuItem but delete operation throws a RuntimeException
        when(menuItemRepository.findById(1)).thenReturn(Optional.of(menuItem));
        doThrow(new RuntimeException("Database error")).when(menuItemRepository).delete(menuItem);

        // When: delete is called
        // Then: RuntimeException is thrown
        assertThrows(RuntimeException.class, () -> {
            menuItemService.delete(1);
        });

        // Then: verify repository interactions
        verify(menuItemRepository).findById(1);
        verify(menuItemRepository).delete(menuItem);
    }

    //edge cases
    @Test
    void createMenuItem_NullInput_ThrowsIllegalArgumentException() {
        // Where / Given: null input
        // When & Then: IllegalArgumentException thrown
        assertThrows(IllegalArgumentException.class, () -> menuItemService.create(1, null));
    }
}

