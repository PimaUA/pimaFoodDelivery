package com.pimaua.core.controller.restaurant;

import com.pimaua.core.dto.ResponseDto;
import com.pimaua.core.dto.enums.EntityType;
import com.pimaua.core.dto.enums.ResponseType;
import com.pimaua.core.dto.restaurant.OpeningHoursRequestDto;
import com.pimaua.core.dto.restaurant.OpeningHoursResponseDto;
import com.pimaua.core.service.restaurant.OpeningHoursService;
import com.pimaua.core.utils.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/opening-hours", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class OpeningHoursController {
    private final OpeningHoursService openingHoursService;

    @PostMapping
    public ResponseEntity<ResponseDto<OpeningHoursResponseDto>>
    createOpeningHours(@Valid @RequestBody OpeningHoursRequestDto openingHoursRequestDto) {
        OpeningHoursResponseDto openingHoursResponseDto = openingHoursService.create(openingHoursRequestDto);
        return ResponseBuilder.buildResponse(ResponseType.CREATED, EntityType.OPENINGHOURS, openingHoursResponseDto);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<OpeningHoursResponseDto>>> findAllOpeningHours() {
        List<OpeningHoursResponseDto> openingHoursList = openingHoursService.findAll();
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS, EntityType.OPENINGHOURS, openingHoursList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<OpeningHoursResponseDto>> findOpeningHours(@PathVariable Integer id) {
        OpeningHoursResponseDto openingHoursResponseDto = openingHoursService.findById(id);
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS, EntityType.OPENINGHOURS, openingHoursResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<OpeningHoursResponseDto>>
    updateOpeningHours(@PathVariable Integer id, @Valid @RequestBody OpeningHoursRequestDto openingHoursRequestDto) {
        OpeningHoursResponseDto openingHoursResponseDto = openingHoursService.update(id, openingHoursRequestDto);
        return ResponseBuilder.buildResponse(ResponseType.UPDATED, EntityType.OPENINGHOURS, openingHoursResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<OpeningHoursResponseDto>> deleteOpeningHours(@PathVariable Integer id) {
        openingHoursService.delete(id);
        return ResponseBuilder.buildResponse(ResponseType.DELETED, EntityType.OPENINGHOURS, null);
    }
}
