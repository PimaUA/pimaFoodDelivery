package com.pimaua.core.service.restaurant;

import com.pimaua.core.dto.restaurant.MenuRequestDto;
import com.pimaua.core.dto.restaurant.MenuResponseDto;
import com.pimaua.core.entity.restaurant.Menu;
import com.pimaua.core.exception.custom.notfound.MenuNotFoundException;
import com.pimaua.core.mapper.restaurant.MenuMapper;
import com.pimaua.core.repository.restaurant.MenuRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper;

    public MenuResponseDto create(MenuRequestDto menuRequestDto) {
        if (menuRequestDto == null) {
            throw new IllegalArgumentException("MenuRequestDto cannot be null");
        }
        Menu menu = menuMapper.toEntity(menuRequestDto);
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
                .orElseThrow(() -> new MenuNotFoundException("Menu not found with ID " + id));
        return menuMapper.toDto(menu);
    }

    public MenuResponseDto update(Integer id, MenuRequestDto menuRequestDto) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new MenuNotFoundException("Menu not found with ID " + id));
        menuMapper.updateEntity(menu, menuRequestDto);
        Menu savedMenu = menuRepository.save(menu);
        return menuMapper.toDto(savedMenu);
    }

    public void delete(Integer id) {
        Menu menu = menuRepository.findById(id)
                .orElseThrow(() -> new MenuNotFoundException("Menu not found with ID " + id));
        menuRepository.delete(menu);
    }
}
