package com.pimaua.core.service.order;

import com.pimaua.core.dto.order.OrderItemRequestDto;
import com.pimaua.core.dto.order.OrderItemResponseDto;
import com.pimaua.core.mapper.order.OrderItemMapper;
import com.pimaua.core.repository.order.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    private final WebClient menuItemWebclient;


    public OrderItemResponseDto createOrderItem(OrderItemRequestDto orderItemRequestDto) {



    }

    public List<OrderItemResponseDto> findAll() {
        return orderItemMapper.toListDto(orderItemRepository.findAll());
    }

    //findById
    public OrderItemResponseDto findById()


    //update

    //delete


}
