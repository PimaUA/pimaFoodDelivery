package com.pimaua.core.dto.customer;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponseDto {
    private Integer id;
    private Integer userId;
    private String name;
    private String phoneNumber;
    private LocalDateTime updatedAt;
}
