package com.pimaua.core.controller.restaurant;

import com.pimaua.core.dto.ResponseDto;
import com.pimaua.core.dto.enums.EntityType;
import com.pimaua.core.dto.enums.ResponseType;
import com.pimaua.core.dto.restaurant.MenuRequestDto;
import com.pimaua.core.dto.restaurant.MenuResponseDto;
import com.pimaua.core.entity.restaurant.Menu;
import com.pimaua.core.service.restaurant.MenuService;
import com.pimaua.core.utils.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="/api/menus", produces= MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class MenuController {
    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<ResponseDto<MenuResponseDto>>
    createMenu(@Valid @RequestBody MenuRequestDto menuRequestDto) {
        MenuResponseDto menuResponseDto = menuService.create(menuRequestDto);
        return ResponseBuilder.buildResponse(ResponseType.CREATED, EntityType.MENU, menuResponseDto);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<MenuResponseDto>>> findAllMenus() {
        List<MenuResponseDto> menusList = menuService.findAll();
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS, EntityType.MENU, menusList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<MenuResponseDto>> findMenu(@PathVariable Integer id) {
        MenuResponseDto menuResponseDto = menuService.findById(id);
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS, EntityType.MENU, menuResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<MenuResponseDto>>
    updateMenu(@PathVariable Integer id, @Valid @RequestBody MenuRequestDto menuRequestDto) {
        MenuResponseDto menuResponseDto = menuService.update(id, menuRequestDto);
        return ResponseBuilder.buildResponse(ResponseType.UPDATED, EntityType.MENU, menuResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<MenuResponseDto>> deleteMenu(@PathVariable Integer id) {
        menuService.delete(id);
        return ResponseBuilder.buildResponse(ResponseType.DELETED, EntityType.MENU, null);
    }
}
