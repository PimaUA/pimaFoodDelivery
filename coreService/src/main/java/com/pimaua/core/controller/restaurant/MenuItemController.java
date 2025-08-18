package com.pimaua.core.controller.restaurant;

import com.pimaua.core.dto.ErrorResponseDto;
import com.pimaua.core.dto.ResponseDto;
import com.pimaua.core.dto.enums.EntityType;
import com.pimaua.core.dto.enums.ResponseType;
import com.pimaua.core.dto.restaurant.MenuItemRequestDto;
import com.pimaua.core.dto.restaurant.MenuItemResponseDto;
import com.pimaua.core.service.restaurant.MenuItemService;
import com.pimaua.core.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "CRUD REST APIs for MenuItem",
        description = "CRUD REST APIs for MenuItem inside CoreService for CREATE,UPDATE,FETCH,DELETE menuItem details"
)
@RestController
@RequestMapping(path="api/menu-items", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class MenuItemController {
    private final MenuItemService menuItemService;

    @Operation(
            summary = "Create menuItem REST API",
            description = "REST API to create menuItem inside CoreService"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "HTTP Status BAD REQUEST",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponseDto.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponseDto.class
                            )
                    )
            )
    }
    )
    @PostMapping
    public ResponseEntity<ResponseDto<MenuItemResponseDto>>
    createMenuItem(@Valid @RequestBody MenuItemRequestDto menuItemRequestDto) {
        MenuItemResponseDto menuItemResponseDto = menuItemService.create(menuItemRequestDto);
        return ResponseBuilder.buildResponse(ResponseType.CREATED, EntityType.MENUITEM, menuItemResponseDto);
    }

    @Operation(
            summary = "Fetch MenuItem Details REST API",
            description = "REST API to fetch all MenuItems' details"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponseDto.class
                            )
                    )

            )
    }
    )
    @GetMapping
    public ResponseEntity<ResponseDto<List<MenuItemResponseDto>>> findAllMenuItems() {
        List<MenuItemResponseDto> menuItemsList = menuItemService.findAll();
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS, EntityType.MENUITEM, menuItemsList);
    }

    @Operation(
            summary = "Fetch specific MenuItem Details REST API",
            description = "REST API to fetch specific MenuItem details using Id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status NOT FOUND",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponseDto.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponseDto.class
                            )
                    )
            )
    }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<MenuItemResponseDto>> findMenuItem(@PathVariable Integer id) {
        MenuItemResponseDto menuItemResponseDto = menuItemService.findById(id);
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS, EntityType.MENUITEM, menuItemResponseDto);
    }

    @Operation(
            summary = "Update specific MenuItem Details REST API",
            description = "REST API to update specific MenuItem details using Id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status NOT FOUND",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponseDto.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponseDto.class
                            )
                    )
            )
    }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<MenuItemResponseDto>>
    updateMenuItem(@PathVariable Integer id, @Valid @RequestBody MenuItemRequestDto menuItemRequestDto) {
        MenuItemResponseDto menuItemResponseDto = menuItemService.update(id, menuItemRequestDto);
        return ResponseBuilder.buildResponse(ResponseType.UPDATED, EntityType.MENUITEM, menuItemResponseDto);
    }

    @Operation(
            summary = "Delete specific MenuItem Details REST API",
            description = "REST API to delete specific MenuItem details using Id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status NOT FOUND",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponseDto.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponseDto.class
                            )
                    )
            )
    }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<MenuItemResponseDto>> deleteMenuItem(@PathVariable Integer id) {
        menuItemService.delete(id);
        return ResponseBuilder.buildResponse(ResponseType.DELETED, EntityType.MENUITEM, null);
    }
}
