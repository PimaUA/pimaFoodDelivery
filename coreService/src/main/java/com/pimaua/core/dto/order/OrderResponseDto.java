package com.pimaua.core.dto.order;

import com.pimaua.core.entity.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {
    private Integer id;
    private Integer userId;
    private Integer restaurantId;
    private OrderStatus orderStatus;
    private Double totalPrice;
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
