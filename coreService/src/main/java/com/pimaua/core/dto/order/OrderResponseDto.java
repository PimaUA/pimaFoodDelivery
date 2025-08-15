package com.pimaua.core.dto.order;

import com.pimaua.core.entity.enums.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDto {
    private Integer id;
    private Integer userId;
    private Integer restaurantId;
    private OrderStatus orderStatus;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private String pickupAddress;
    private BigDecimal pickupLatitude;
    private BigDecimal pickupLongitude;
    private String dropOffAddress;
    private BigDecimal dropOffLatitude;
    private BigDecimal dropOffLongitude;
    private List<OrderItemResponseDto> orderItems;
    private LocalDateTime updatedAt;
}
