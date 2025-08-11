package com.pimaua.core.dto.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDto {
    private Integer id;
    private Integer userId;
    private String name;
    private String phoneNumber;
    private LocalDateTime updatedAt;
}
