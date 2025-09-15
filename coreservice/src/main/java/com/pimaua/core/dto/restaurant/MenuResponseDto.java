package com.pimaua.core.dto.restaurant;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuResponseDto {
    private Integer id;
    private String name;
    private Boolean isActive;
    private List<MenuItemResponseDto> menuItems;
    private LocalDateTime updatedAt;
}
