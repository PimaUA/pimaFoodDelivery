package com.pimaua.core.service.restaurant;

import com.pimaua.core.dto.restaurant.MenuRequestDto;
import com.pimaua.core.dto.restaurant.MenuResponseDto;
import com.pimaua.core.entity.restaurant.Menu;
import com.pimaua.core.entity.restaurant.MenuItem;
import com.pimaua.core.entity.restaurant.Restaurant;
import com.pimaua.core.exception.custom.notfound.MenuItemNotFoundException;
import com.pimaua.core.exception.custom.notfound.MenuNotFoundException;
import com.pimaua.core.mapper.restaurant.MenuItemMapper;
import com.pimaua.core.mapper.restaurant.MenuMapper;
import com.pimaua.core.repository.restaurant.MenuRepository;
import com.pimaua.core.repository.restaurant.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper;
    private final MenuItemMapper menuItemMapper;
    private final RestaurantRepository restaurantRepository;
    private static final Logger logger = LoggerFactory.getLogger(MenuService.class);

    public MenuResponseDto create(MenuRequestDto menuRequestDto) {
        if (menuRequestDto == null) {
            logger.error("Failed to create Menu, no input");
            throw new IllegalArgumentException("MenuRequestDto cannot be null");
        }
        Restaurant restaurant = restaurantRepository.findById(menuRequestDto.getRestaurantId())
                .orElseThrow(() -> {
                    logger.error("Restaurant not found with id={}", menuRequestDto.getRestaurantId());
                    return new MenuItemNotFoundException("Restaurant not found with ID " + menuRequestDto.getRestaurantId());
                });
        Menu menu = menuMapper.toEntity(menuRequestDto);
        menu.setRestaurant(restaurant);
        setMenuForItemsOnCreate(menu);

        Menu savedMenu = menuRepository.save(menu);
        return menuMapper.toDto(savedMenu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponseDto> findAll() {
        List<Menu> menus = menuRepository.findAll();
        return menuMapper.toListDto(menus);
    }

    @Transactional(readOnly = true)
    public MenuResponseDto findById(Integer id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Menu not found with id={}", id);
                    return new MenuNotFoundException("Menu not found with ID " + id);
                });
        return menuMapper.toDto(menu);
    }

    public MenuResponseDto update(Integer id, MenuRequestDto menuRequestDto) {
        if (menuRequestDto == null) {
            logger.error("Failed to update Menu, no input");
            throw new IllegalArgumentException("MenuRequestDto cannot be null");
        }
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Menu not found with id={}", id);
                    return new MenuNotFoundException("Menu not found with ID " + id);
                });
        menuMapper.updateEntity(menu, menuRequestDto);
        setMenuForItemsOnUpdate(menu);

        Menu savedMenu = menuRepository.save(menu);
        return menuMapper.toDto(savedMenu);
    }

    public void delete(Integer id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Menu not found with id={}", id);
                    return new MenuNotFoundException("Menu not found with ID " + id);
                });
        menuRepository.delete(menu);
    }

    // For CREATE operations - generate IDs for items without them
    private void setMenuForItemsOnCreate(Menu menu) {
        if (menu.getMenuItems() != null) {
            for (MenuItem item : menu.getMenuItems()) {
                item.setMenu(menu);
                if (item.getId() == null) {
                    item.setId(generateUniqueId());
                    logger.debug("Generated ID {} for new menu item {}", item.getId(), item.getName());
                }
            }
        }
    }

    // For UPDATE operations - preserve existing IDs, only generate for new items
    private void setMenuForItemsOnUpdate(Menu menu) {
        if (menu.getMenuItems() != null) {
            for (MenuItem item : menu.getMenuItems()) {
                item.setMenu(menu);
                // Only generate ID if it's truly null (new item being added during update)
                if (item.getId() == null) {
                    item.setId(generateUniqueId());
                    logger.debug("Generated ID {} for new menu item added during update", item.getId());
                } else {
                    logger.debug("Preserving existing ID {} for menu item {}", item.getId(), item.getName());
                }
                item.setUpdatedAt(LocalDateTime.now());
            }
        }
    }

    private Integer generateUniqueId() {
        return Math.abs(UUID.randomUUID().hashCode());
    }
}
