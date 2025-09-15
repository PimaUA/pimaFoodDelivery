package com.pimaua.core.dto.order;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderUpdateDto {
    private String pickupAddress;
    private BigDecimal pickupLatitude;
    private BigDecimal pickupLongitude;
    private String dropOffAddress;
    private BigDecimal dropOffLatitude;
    private BigDecimal dropOffLongitude;
   }
