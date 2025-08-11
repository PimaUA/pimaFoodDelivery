package com.pimaua.core.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateDto {
    @NotNull
    private Integer restaurantId;
    @NotBlank
    private String pickupAddress;
    @NotNull
    private BigDecimal pickupLatitude;
    @NotNull
    private BigDecimal pickupLongitude;
    @NotBlank
    private String dropOffAddress;
    @NotNull
    private BigDecimal dropOffLatitude;
    @NotNull
    private BigDecimal dropOffLongitude;
    @NotNull
    @NotEmpty
    private List<OrderItemRequestDto> orderItems;
}
