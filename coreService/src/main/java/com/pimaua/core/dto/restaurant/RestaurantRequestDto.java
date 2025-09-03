package com.pimaua.core.dto.restaurant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantRequestDto {
    @NotBlank
    @Size(max=50)
    private String name;
    private String description;
    @NotBlank
    @Size(max=50)
    private String address;
    private Boolean isActive;
}
