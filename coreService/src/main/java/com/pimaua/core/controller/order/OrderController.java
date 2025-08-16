package com.pimaua.core.controller.order;

import com.pimaua.core.dto.ResponseDto;
import com.pimaua.core.dto.enums.EntityType;
import com.pimaua.core.dto.enums.ResponseType;
import com.pimaua.core.dto.order.OrderCreateDto;
import com.pimaua.core.dto.order.OrderResponseDto;
import com.pimaua.core.dto.order.OrderUpdateDto;
import com.pimaua.core.service.order.OrderService;
import com.pimaua.core.utils.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/orders", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ResponseDto<OrderResponseDto>>
    createOrder(@Valid @RequestBody OrderCreateDto orderCreateDto, @RequestParam Integer userId) {  //later replace with@AuthenticationPrincipal CustomUserDetails userDetails)

        OrderResponseDto orderResponseDto = orderService.create(orderCreateDto, userId);
        return ResponseBuilder.buildResponse(ResponseType.CREATED, EntityType.ORDER, orderResponseDto);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<OrderResponseDto>>> findAllOrders() {
        List<OrderResponseDto> orders = orderService.findAll();
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS, EntityType.ORDER, orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<OrderResponseDto>> findOrder(@PathVariable Integer id) {
        OrderResponseDto orderResponseDto = orderService.findById(id);
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS, EntityType.ORDER, orderResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<OrderResponseDto>>
    updateOrder(@PathVariable Integer id, @Valid @RequestBody OrderUpdateDto orderUpdateDto) {
        OrderResponseDto orderResponseDto = orderService.update(id, orderUpdateDto);
        return ResponseBuilder.buildResponse(ResponseType.UPDATED, EntityType.ORDER, orderResponseDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseDto<OrderResponseDto>> recalculateOrderTotalPrice(@PathVariable Integer id) {
        OrderResponseDto orderResponseDto = orderService.recalculateTotalPrice(id);
        return ResponseBuilder.buildResponse(ResponseType.UPDATED, EntityType.ORDER, orderResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<OrderResponseDto>> deleteOrder(@PathVariable Integer id) {
        orderService.delete(id);
        return ResponseBuilder.buildResponse(ResponseType.DELETED, EntityType.ORDER, null);
    }
}
