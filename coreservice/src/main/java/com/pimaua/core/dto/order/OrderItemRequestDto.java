package com.pimaua.core.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemRequestDto {
    @NotNull
    @Min(1)
    private Integer menuItemId;
    @NotNull
    @Min(1)
    private Integer quantity;
}
