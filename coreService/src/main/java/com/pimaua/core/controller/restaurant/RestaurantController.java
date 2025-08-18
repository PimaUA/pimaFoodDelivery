package com.pimaua.core.controller.restaurant;

import com.pimaua.core.dto.ErrorResponseDto;
import com.pimaua.core.dto.ResponseDto;
import com.pimaua.core.dto.enums.EntityType;
import com.pimaua.core.dto.enums.ResponseType;
import com.pimaua.core.dto.restaurant.RestaurantRequestDto;
import com.pimaua.core.dto.restaurant.RestaurantResponseDto;
import com.pimaua.core.service.restaurant.RestaurantService;
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
        name = "CRUD REST APIs for Restaurant",
        description = "CRUD REST APIs for Restaurant inside CoreService for CREATE,UPDATE,FETCH,DELETE restaurant details"
)
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class RestaurantController {
    private final RestaurantService restaurantService;

    @Operation(
            summary = "Create restaurant REST API",
            description = "REST API to create restaurant inside CoreService"
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
    public ResponseEntity<ResponseDto<RestaurantResponseDto>>
    createRestaurant(@Valid @RequestBody RestaurantRequestDto restaurantRequestDto) {
        RestaurantResponseDto restaurantResponseDto = restaurantService.create(restaurantRequestDto);
        return ResponseBuilder.buildResponse(ResponseType.CREATED, EntityType.RESTAURANT, restaurantResponseDto);
    }

    @Operation(
            summary = "Fetch Restaurants Details REST API",
            description = "REST API to fetch all Restaurants' details"
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
    public ResponseEntity<ResponseDto<List<RestaurantResponseDto>>> findAllRestaurants() {
        List<RestaurantResponseDto> restaurantsList = restaurantService.findAll();
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS, EntityType.RESTAURANT, restaurantsList);
    }

    @Operation(
            summary = "Fetch specific Restaurant Details REST API",
            description = "REST API to fetch specific Restaurant details using Id"
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
    public ResponseEntity<ResponseDto<RestaurantResponseDto>> findRestaurant(@PathVariable Integer id) {
        RestaurantResponseDto restaurantResponseDto = restaurantService.findById(id);
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS, EntityType.RESTAURANT, restaurantResponseDto);
    }

    @Operation(
            summary = "Update specific Restaurant Details REST API",
            description = "REST API to update specific Restaurant details using Id"
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
    public ResponseEntity<ResponseDto<RestaurantResponseDto>>
    updateRestaurant(@PathVariable Integer id, @Valid @RequestBody RestaurantRequestDto restaurantRequestDto) {
        RestaurantResponseDto restaurantResponseDto = restaurantService.update(id, restaurantRequestDto);
        return ResponseBuilder.buildResponse(ResponseType.UPDATED, EntityType.RESTAURANT, restaurantResponseDto);
    }

    @Operation(
            summary = "Delete specific Restaurant Details REST API",
            description = "REST API to delete specific Restaurant details using Id"
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
    public ResponseEntity<ResponseDto<RestaurantResponseDto>> deleteRestaurant(@PathVariable Integer id) {
        restaurantService.delete(id);
        return ResponseBuilder.buildResponse(ResponseType.DELETED, EntityType.RESTAURANT, null);
    }
}
