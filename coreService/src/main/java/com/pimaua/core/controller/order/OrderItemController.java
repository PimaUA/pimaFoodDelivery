package com.pimaua.core.controller.order;

import com.pimaua.core.dto.ResponseDto;
import com.pimaua.core.dto.enums.EntityType;
import com.pimaua.core.dto.enums.ResponseType;
import com.pimaua.core.dto.order.OrderItemRequestDto;
import com.pimaua.core.dto.order.OrderItemResponseDto;
import com.pimaua.core.entity.order.OrderItem;
import com.pimaua.core.mapper.order.OrderItemMapper;
import com.pimaua.core.service.order.OrderItemService;
import com.pimaua.core.utils.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/order-items", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class OrderItemController {
    private final OrderItemService orderItemService;
    private final OrderItemMapper orderItemMapper;

    @PostMapping
    public ResponseEntity<ResponseDto<OrderItemResponseDto>>
    createOrderItem(@Valid @RequestBody OrderItemRequestDto orderItemRequestDto) {
        OrderItem orderItem = orderItemService.create(orderItemRequestDto);
        OrderItemResponseDto orderItemResponseDto=orderItemMapper.toDto(orderItem);
        return ResponseBuilder.buildResponse(ResponseType.CREATED, EntityType.ORDERITEM, orderItemResponseDto);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<OrderItemResponseDto>>> findAllOrderItems() {
        List<OrderItemResponseDto> orderItems = orderItemService.findAll();
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS, EntityType.ORDERITEM, orderItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<OrderItemResponseDto>> findOrderItem(@PathVariable Integer id) {
        OrderItemResponseDto orderItemResponseDto = orderItemService.findById(id);
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS, EntityType.ORDERITEM, orderItemResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<OrderItemResponseDto>>
    updateOrderItem(@PathVariable Integer id, @Valid @RequestBody OrderItemRequestDto orderItemRequestDto) {
        OrderItemResponseDto orderItemResponseDto = orderItemService.update(id, orderItemRequestDto);
        return ResponseBuilder.buildResponse(ResponseType.UPDATED, EntityType.ORDERITEM, orderItemResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<OrderItemResponseDto>> deleteOrderItem(@PathVariable Integer id) {
        orderItemService.delete(id);
        return ResponseBuilder.buildResponse(ResponseType.DELETED, EntityType.ORDERITEM, null);
    }
}
