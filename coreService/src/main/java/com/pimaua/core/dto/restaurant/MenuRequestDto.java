package com.pimaua.core.dto.restaurant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MenuRequestDto {
    @NotBlank
    private String name;
    private Boolean isActive;
    @NotNull
    @NotEmpty
    private List<MenuItemRequestDto> menuItems;
}
