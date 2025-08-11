package com.pimaua.core.dto.restaurant;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantRequestDto {
    @NotBlank
    private String name;
    private String description;
    @NotBlank
    private String address;
    private Boolean isActive;
}
