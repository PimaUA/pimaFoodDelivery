package com.pimaua.core.service.restaurant;

import com.pimaua.core.dto.restaurant.MenuItemRequestDto;
import com.pimaua.core.dto.restaurant.MenuItemResponseDto;
import com.pimaua.core.entity.restaurant.Menu;
import com.pimaua.core.entity.restaurant.MenuItem;
import com.pimaua.core.exception.custom.notfound.MenuItemNotFoundException;
import com.pimaua.core.mapper.restaurant.MenuItemMapper;
import com.pimaua.core.repository.restaurant.MenuItemRepository;
import com.pimaua.core.repository.restaurant.MenuRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;
    private final MenuItemMapper menuItemMapper;
    private final MenuRepository menuRepository;
    private static final Logger logger = LoggerFactory.getLogger(MenuItemService.class);

    public MenuItemResponseDto create(MenuItemRequestDto menuItemRequestDto) {
        if (menuItemRequestDto == null) {
            logger.error("Failed to create MenuItem, no input");
            throw new IllegalArgumentException("MenuItemRequestDto cannot be null");
        }

        Menu menu = menuRepository.findById(menuItemRequestDto.getMenuId())
                .orElseThrow(() -> {
                    logger.error("Menu not found with id={}", menuItemRequestDto.getMenuId());
                    return new MenuItemNotFoundException("Menu not found with ID " + menuItemRequestDto.getMenuId());
                });

        MenuItem menuItem = menuItemMapper.toEntity(menuItemRequestDto);
        menuItem.setMenu(menu);
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return menuItemMapper.toDto(savedMenuItem);
    }

    public List<MenuItemResponseDto> findAll() {
        List<MenuItem> menuItems = menuItemRepository.findAll();
        return menuItemMapper.toListDto(menuItems);
    }

    public MenuItemResponseDto findById(Integer id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Order not found with id={}", id);
                    return new MenuItemNotFoundException("MenuItem not found with ID " + id);
                });
        return menuItemMapper.toDto(menuItem);
    }

    public MenuItem getMenuItemById(Integer id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Order not found with id={}", id);
                    return new MenuItemNotFoundException("MenuItem not found with ID " + id);
                });
    }

    public MenuItemResponseDto update(Integer id, MenuItemRequestDto menuItemRequestDto) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Order not found with id={}", id);
                    return new MenuItemNotFoundException("MenuItem not found with ID " + id);
                });
        menuItemMapper.updateEntity(menuItem, menuItemRequestDto);
        MenuItem savedMenuItem = menuItemRepository.save(menuItem);
        return menuItemMapper.toDto(savedMenuItem);
    }

    public void delete(Integer id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> {
            logger.error("Order not found with id={}", id);
            return new MenuItemNotFoundException("MenuItem not found with ID " + id);
        });
        menuItemRepository.delete(menuItem);
    }
}
