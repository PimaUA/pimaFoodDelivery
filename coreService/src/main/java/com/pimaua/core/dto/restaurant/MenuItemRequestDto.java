package com.pimaua.core.dto.restaurant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuItemRequestDto {
    @NotNull
    private Integer id;
    @NotBlank
    private String name;
    private String description;
    @NotNull
    @Positive
    private BigDecimal price;
    private Boolean isAvailable;
    @NotNull
    private Integer menuId;
}
