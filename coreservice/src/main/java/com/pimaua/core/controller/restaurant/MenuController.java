package com.pimaua.core.controller.restaurant;

import com.pimaua.core.dto.ErrorResponseDto;
import com.pimaua.core.dto.ResponseDto;
import com.pimaua.core.dto.enums.EntityType;
import com.pimaua.core.dto.enums.ResponseType;
import com.pimaua.core.dto.restaurant.MenuRequestDto;
import com.pimaua.core.dto.restaurant.MenuResponseDto;
import com.pimaua.core.service.restaurant.MenuService;
import com.pimaua.core.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "CRUD REST APIs for Menu",
        description = "CRUD REST APIs for Menu inside CoreService for CREATE,UPDATE,FETCH,DELETE menu details"
)
@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class MenuController {
    private final MenuService menuService;

    @Operation(
            summary = "Create Menu REST API",
            description = "REST API to create Menu inside CoreService"
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
    @PostMapping("/admin/restaurants/{restaurantId}/menu")
    public ResponseEntity<ResponseDto<MenuResponseDto>>
    createMenu(@PathVariable Integer restaurantId,
               @Valid @RequestBody MenuRequestDto menuRequestDto) {
        MenuResponseDto menuResponseDto = menuService.create(restaurantId, menuRequestDto);
        return ResponseBuilder.buildResponse(ResponseType.CREATED, EntityType.MENU, menuResponseDto);
    }

    @Operation(
            summary = "Fetch Menu Details REST API",
            description = "REST API to fetch all Menu details"
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
    @GetMapping("restaurants/{id}/menu")
    public ResponseEntity<ResponseDto<Page<MenuResponseDto>>> findAllMenusForRestaurant
            (@PathVariable Integer id,
             @RequestParam(defaultValue = "true") boolean active,
             @PageableDefault(page = 0, size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<MenuResponseDto> menusList = menuService.findAllByRestaurantId(id, active, pageable);
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS, EntityType.MENU, menusList);
    }

    @Operation(
            summary = "Fetch specific Menu Details REST API",
            description = "REST API to fetch specific Menu details using Id"
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
    @GetMapping("/menu/{id}")
    public ResponseEntity<ResponseDto<MenuResponseDto>> findMenu(@PathVariable Integer id) {
        MenuResponseDto menuResponseDto = menuService.findById(id);
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS, EntityType.MENU, menuResponseDto);
    }

    @Operation(
            summary = "Update specific Menu Details REST API",
            description = "REST API to update specific Menu details using Id"
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
    @PutMapping("/admin/menu/{id}")
    public ResponseEntity<ResponseDto<MenuResponseDto>>
    updateMenu(@PathVariable Integer id, @Valid @RequestBody MenuRequestDto menuRequestDto) {
        MenuResponseDto menuResponseDto = menuService.update(id, menuRequestDto);
        return ResponseBuilder.buildResponse(ResponseType.UPDATED, EntityType.MENU, menuResponseDto);
    }

    @Operation(
            summary = "Delete specific Menu Details REST API",
            description = "REST API to delete specific Menu details using Id"
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
    @DeleteMapping("/admin/menu/{id}")
    public ResponseEntity<ResponseDto<MenuResponseDto>> deleteMenu(@PathVariable Integer id) {
        menuService.delete(id);
        return ResponseBuilder.buildResponse(ResponseType.DELETED, EntityType.MENU, null);
    }
}
