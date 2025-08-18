package com.pimaua.core.controller.restaurant;

import com.pimaua.core.dto.ErrorResponseDto;
import com.pimaua.core.dto.ResponseDto;
import com.pimaua.core.dto.enums.EntityType;
import com.pimaua.core.dto.enums.ResponseType;
import com.pimaua.core.dto.restaurant.OpeningHoursRequestDto;
import com.pimaua.core.dto.restaurant.OpeningHoursResponseDto;
import com.pimaua.core.service.restaurant.OpeningHoursService;
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
        name = "CRUD REST APIs for OpeningHours",
        description = "CRUD REST APIs for OpeningHours inside CoreService " +
                "for CREATE,UPDATE,FETCH,DELETE opening hours details"
)
@RestController
@RequestMapping(path = "/api/opening-hours", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class OpeningHoursController {
    private final OpeningHoursService openingHoursService;

    @Operation(
            summary = "Create openingHours REST API",
            description = "REST API to create openingHours inside CoreService"
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
    public ResponseEntity<ResponseDto<OpeningHoursResponseDto>>
    createOpeningHours(@Valid @RequestBody OpeningHoursRequestDto openingHoursRequestDto) {
        OpeningHoursResponseDto openingHoursResponseDto = openingHoursService.create(openingHoursRequestDto);
        return ResponseBuilder.buildResponse(ResponseType.CREATED, EntityType.OPENINGHOURS, openingHoursResponseDto);
    }

    @Operation(
            summary = "Fetch OpeningHours Details REST API",
            description = "REST API to fetch all OpeningHours details available"
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
    public ResponseEntity<ResponseDto<List<OpeningHoursResponseDto>>> findAllOpeningHours() {
        List<OpeningHoursResponseDto> openingHoursList = openingHoursService.findAll();
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS, EntityType.OPENINGHOURS, openingHoursList);
    }

    @Operation(
            summary = "Fetch specific OpeningHours Details REST API",
            description = "REST API to fetch specific OpeningHours details using Id"
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
    public ResponseEntity<ResponseDto<OpeningHoursResponseDto>> findOpeningHours(@PathVariable Integer id) {
        OpeningHoursResponseDto openingHoursResponseDto = openingHoursService.findById(id);
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS, EntityType.OPENINGHOURS, openingHoursResponseDto);
    }

    @Operation(
            summary = "Update specific OpeningHours Details REST API",
            description = "REST API to update specific OpeningHours details using Id"
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
    public ResponseEntity<ResponseDto<OpeningHoursResponseDto>>
    updateOpeningHours(@PathVariable Integer id, @Valid @RequestBody OpeningHoursRequestDto openingHoursRequestDto) {
        OpeningHoursResponseDto openingHoursResponseDto = openingHoursService.update(id, openingHoursRequestDto);
        return ResponseBuilder.buildResponse(ResponseType.UPDATED, EntityType.OPENINGHOURS, openingHoursResponseDto);
    }

    @Operation(
            summary = "Delete specific OpeningHours Details REST API",
            description = "REST API to delete specific OpeningHours details using Id"
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
    public ResponseEntity<ResponseDto<OpeningHoursResponseDto>> deleteOpeningHours(@PathVariable Integer id) {
        openingHoursService.delete(id);
        return ResponseBuilder.buildResponse(ResponseType.DELETED, EntityType.OPENINGHOURS, null);
    }
}
