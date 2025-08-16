package com.pimaua.core.controller.restaurant;

import com.pimaua.core.dto.ResponseDto;
import com.pimaua.core.dto.enums.EntityType;
import com.pimaua.core.dto.enums.ResponseType;
import com.pimaua.core.dto.restaurant.MenuItemRequestDto;
import com.pimaua.core.dto.restaurant.MenuItemResponseDto;
import com.pimaua.core.service.restaurant.MenuItemService;
import com.pimaua.core.utils.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/menu-items", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class MenuItemController {
    private final MenuItemService menuItemService;

    @PostMapping
    public ResponseEntity<ResponseDto<MenuItemResponseDto>>
    createMenuItem(@Valid @RequestBody MenuItemRequestDto menuItemRequestDto) {
        MenuItemResponseDto menuItemResponseDto = menuItemService.create(menuItemRequestDto);
        return ResponseBuilder.buildResponse(ResponseType.CREATED, EntityType.MENUITEM, menuItemResponseDto);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<MenuItemResponseDto>>> findAllMenuItems() {
        List<MenuItemResponseDto> menuItemsList = menuItemService.findAll();
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS, EntityType.MENUITEM, menuItemsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<MenuItemResponseDto>> findMenuItem(@PathVariable Integer id) {
        MenuItemResponseDto menuItemResponseDto = menuItemService.findById(id);
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS, EntityType.MENUITEM, menuItemResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<MenuItemResponseDto>>
    updateMenuItem(@PathVariable Integer id, @Valid @RequestBody MenuItemRequestDto menuItemRequestDto) {
        MenuItemResponseDto menuItemResponseDto = menuItemService.update(id, menuItemRequestDto);
        return ResponseBuilder.buildResponse(ResponseType.UPDATED, EntityType.MENUITEM, menuItemResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<MenuItemResponseDto>> deleteMenuItem(@PathVariable Integer id) {
        menuItemService.delete(id);
        return ResponseBuilder.buildResponse(ResponseType.DELETED, EntityType.MENUITEM, null);
    }
}
