package com.pimaua.core.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequestDto {
    @NotNull
    @Min(1)
    private Integer menuItemId;
    @NotNull
    @Min(1)
    private Integer quantity;
}
