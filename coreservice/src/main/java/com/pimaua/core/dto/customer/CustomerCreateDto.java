package com.pimaua.core.dto.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerCreateDto {
    @NotNull(message = "User ID is required")
    private Integer userId;
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2-100 characters")
    private String name;
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{6,14}$",
            message = "Phone number must be 7-15 digits, optionally starting with +")
    private String phoneNumber;
}
