package com.pimaua.core.dto.restaurant;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemResponseDto {
    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean isAvailable;
    private LocalDateTime updatedAt;
}



