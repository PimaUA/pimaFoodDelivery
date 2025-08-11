package com.pimaua.core.dto.order;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseDto {
    private Integer id;
    private Integer menuItemId;
    private String name;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
}
