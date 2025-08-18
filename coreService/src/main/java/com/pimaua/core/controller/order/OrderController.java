package com.pimaua.core.controller.order;

import com.pimaua.core.dto.ErrorResponseDto;
import com.pimaua.core.dto.ResponseDto;
import com.pimaua.core.dto.enums.EntityType;
import com.pimaua.core.dto.enums.ResponseType;
import com.pimaua.core.dto.order.OrderCreateDto;
import com.pimaua.core.dto.order.OrderResponseDto;
import com.pimaua.core.dto.order.OrderUpdateDto;
import com.pimaua.core.service.order.OrderService;
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
        name = "CRUD REST APIs for Order",
        description = "CRUD REST APIs for Order inside CoreService for CREATE,UPDATE,FETCH,DELETE order details"
)
@RestController
@RequestMapping(path = "api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;

    @Operation(
            summary = "Create Order REST API",
            description = "REST API to create Order inside CoreService"
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
    public ResponseEntity<ResponseDto<OrderResponseDto>>
    createOrder(@Valid @RequestBody OrderCreateDto orderCreateDto, @RequestParam Integer userId) {  //later replace with@AuthenticationPrincipal CustomUserDetails userDetails)

        OrderResponseDto orderResponseDto = orderService.create(orderCreateDto, userId);
        return ResponseBuilder.buildResponse(ResponseType.CREATED, EntityType.ORDER, orderResponseDto);
    }

    @Operation(
            summary = "Fetch Orders Details REST API",
            description = "REST API to fetch all Orders' details"
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
    public ResponseEntity<ResponseDto<List<OrderResponseDto>>> findAllOrders() {
        List<OrderResponseDto> orders = orderService.findAll();
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS, EntityType.ORDER, orders);
    }

    @Operation(
            summary = "Fetch specific Order Details REST API",
            description = "REST API to fetch specific Order details using Id"
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
    public ResponseEntity<ResponseDto<OrderResponseDto>> findOrder(@PathVariable Integer id) {
        OrderResponseDto orderResponseDto = orderService.findById(id);
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS, EntityType.ORDER, orderResponseDto);
    }

    @Operation(
            summary = "Complete update specific Order Details REST API",
            description = "REST API to update specific Order details using Id"
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
    public ResponseEntity<ResponseDto<OrderResponseDto>>
    updateOrder(@PathVariable Integer id, @Valid @RequestBody OrderUpdateDto orderUpdateDto) {
        OrderResponseDto orderResponseDto = orderService.update(id, orderUpdateDto);
        return ResponseBuilder.buildResponse(ResponseType.UPDATED, EntityType.ORDER, orderResponseDto);
    }

    @Operation(
            summary = "Partial update specific Order Details REST API",
            description = "REST API to update specific Order details using Id"
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
    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDto<OrderResponseDto>> recalculateOrderTotalPrice(@PathVariable Integer id) {
        OrderResponseDto orderResponseDto = orderService.recalculateTotalPrice(id);
        return ResponseBuilder.buildResponse(ResponseType.UPDATED, EntityType.ORDER, orderResponseDto);
    }

    @Operation(
            summary = "Delete specific Order Details REST API",
            description = "REST API to delete specific Order details using Id"
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
    public ResponseEntity<ResponseDto<OrderResponseDto>> deleteOrder(@PathVariable Integer id) {
        orderService.delete(id);
        return ResponseBuilder.buildResponse(ResponseType.DELETED, EntityType.ORDER, null);
    }
}
