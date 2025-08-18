package com.pimaua.core.controller.order;

import com.pimaua.core.dto.ErrorResponseDto;
import com.pimaua.core.dto.ResponseDto;
import com.pimaua.core.dto.enums.EntityType;
import com.pimaua.core.dto.enums.ResponseType;
import com.pimaua.core.dto.order.OrderItemRequestDto;
import com.pimaua.core.dto.order.OrderItemResponseDto;
import com.pimaua.core.dto.order.OrderResponseDto;
import com.pimaua.core.mapper.order.OrderItemMapper;
import com.pimaua.core.service.order.OrderItemService;
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
        name = "CRUD REST APIs for OrderItem",
        description = "CRUD REST APIs for OrderItem inside CoreService for CREATE,UPDATE,FETCH,DELETE orderItem details"
)
@RestController
@RequestMapping(path = "api/order-items", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class OrderItemController {
    private final OrderItemService orderItemService;
    private final OrderItemMapper orderItemMapper;

    @Operation(
            summary = "Add OrderItem to Order REST API",
            description = "REST API to add OrderItem to Order inside CoreService"
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
    @PostMapping("/{orderId}/item")
    public ResponseEntity<ResponseDto<OrderItemResponseDto>> addItemToOrder(
            @PathVariable Integer orderId,
            @Valid @RequestBody OrderItemRequestDto orderItemRequestDto) {
        OrderItemResponseDto orderItemResponseDto = orderItemService.addItemToExistingOrder(orderId,orderItemRequestDto);
        return ResponseBuilder.buildResponse(ResponseType.CREATED, EntityType.ORDERITEM, orderItemResponseDto);
    }

    @Operation(
            summary = "Fetch OrderItems Details REST API",
            description = "REST API to fetch all OrdersItems' details"
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
    public ResponseEntity<ResponseDto<List<OrderItemResponseDto>>> findAllOrderItems() {
        List<OrderItemResponseDto> orderItems = orderItemService.findAll();
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS, EntityType.ORDERITEM, orderItems);
    }

    @Operation(
            summary = "Fetch specific OrderItem details REST API",
            description = "REST API to fetch specific OrderItem details related to Order using OrderId"
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
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ResponseDto<List<OrderItemResponseDto>>> getOrderItemsByOrder(@PathVariable Integer orderId) {
        List<OrderItemResponseDto>orderItems=orderItemService.findByOrderId(orderId);
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS, EntityType.ORDERITEM, orderItems);
    }

    @Operation(
            summary = "Fetch specific OrderItem Details REST API",
            description = "REST API to fetch specific OrderItems details using Id"
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
    public ResponseEntity<ResponseDto<OrderItemResponseDto>> findOrderItem(@PathVariable Integer id) {
        OrderItemResponseDto orderItemResponseDto = orderItemService.findById(id);
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS, EntityType.ORDERITEM, orderItemResponseDto);
    }

    @Operation(
            summary = "Update specific OrderItem Details REST API",
            description = "REST API to update specific OrderItem details using Id"
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
    public ResponseEntity<ResponseDto<OrderItemResponseDto>>
    updateOrderItem(@PathVariable Integer id, @Valid @RequestBody OrderItemRequestDto orderItemRequestDto) {
        OrderItemResponseDto orderItemResponseDto = orderItemService.update(id, orderItemRequestDto);
        return ResponseBuilder.buildResponse(ResponseType.UPDATED, EntityType.ORDERITEM, orderItemResponseDto);
    }

    @Operation(
            summary = "Delete specific OrderItem Details REST API",
            description = "REST API to delete specific OrderItem details using Id"
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
    public ResponseEntity<ResponseDto<OrderItemResponseDto>> deleteOrderItem(@PathVariable Integer id) {
        orderItemService.delete(id);
        return ResponseBuilder.buildResponse(ResponseType.DELETED, EntityType.ORDERITEM, null);
    }
}
