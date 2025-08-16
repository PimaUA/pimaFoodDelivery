package com.pimaua.core.controller.restaurant;

import com.pimaua.core.dto.ResponseDto;
import com.pimaua.core.dto.enums.EntityType;
import com.pimaua.core.dto.enums.ResponseType;
import com.pimaua.core.dto.restaurant.RestaurantRequestDto;
import com.pimaua.core.dto.restaurant.RestaurantResponseDto;
import com.pimaua.core.service.restaurant.RestaurantService;
import com.pimaua.core.utils.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/restaurants", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class RestaurantController {
    private final RestaurantService restaurantService;

    @PostMapping
    public ResponseEntity<ResponseDto<RestaurantResponseDto>>
    createRestaurant(@Valid @RequestBody RestaurantRequestDto restaurantRequestDto) {
        RestaurantResponseDto restaurantResponseDto = restaurantService.create(restaurantRequestDto);
        return ResponseBuilder.buildResponse(ResponseType.CREATED, EntityType.RESTAURANT, restaurantResponseDto);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<RestaurantResponseDto>>> findAllRestaurants() {
        List<RestaurantResponseDto> restaurantsList = restaurantService.findAll();
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS, EntityType.RESTAURANT, restaurantsList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<RestaurantResponseDto>> findRestaurant(@PathVariable Integer id) {
        RestaurantResponseDto restaurantResponseDto = restaurantService.findById(id);
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS, EntityType.RESTAURANT, restaurantResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<RestaurantResponseDto>>
    updateRestaurant(@PathVariable Integer id, @Valid @RequestBody RestaurantRequestDto restaurantRequestDto) {
        RestaurantResponseDto restaurantResponseDto = restaurantService.update(id, restaurantRequestDto);
        return ResponseBuilder.buildResponse(ResponseType.UPDATED, EntityType.RESTAURANT, restaurantResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<RestaurantResponseDto>> deleteRestaurant(@PathVariable Integer id) {
        restaurantService.delete(id);
        return ResponseBuilder.buildResponse(ResponseType.DELETED, EntityType.RESTAURANT, null);
    }
}
