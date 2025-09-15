package com.pimaua.core.dto.restaurant;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantResponseDto {
    private Integer id;
    private String name;
    private String description;
    private String address;
    private Boolean isActive;
    private LocalDateTime updatedAt;
}
