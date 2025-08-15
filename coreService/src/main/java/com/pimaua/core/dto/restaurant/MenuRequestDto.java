package com.pimaua.core.dto.restaurant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuRequestDto {
    @NotBlank
    private String name;
    private Boolean isActive;
    @NotNull
    @NotEmpty
    private List<MenuItemRequestDto> menuItems;
}
