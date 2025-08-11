package com.pimaua.core.dto.restaurant;

import com.pimaua.core.entity.restaurant.MenuItem;
import com.pimaua.core.entity.restaurant.Restaurant;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuResponseDto {
    private Integer id;
    private String name;
    private Boolean isActive;
    private List<MenuItemResponseDto> menuItems;
    private LocalDateTime updatedAt;
}
