package com.pimaua.core.service.customer.testdata;

import com.pimaua.core.dto.customer.CustomerCreateDto;
import com.pimaua.core.dto.customer.CustomerResponseDto;
import com.pimaua.core.dto.customer.CustomerUpdateDto;
import com.pimaua.core.entity.customer.Customer;

import java.time.LocalDateTime;

public class CustomerTestData {

    public static Customer mockCustomer() {
        return Customer.builder()
                .id(2)
                .userId(1)
                .name("John")
                .phoneNumber("0637490343")
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static CustomerCreateDto mockCustomerCreateDto() {
        return CustomerCreateDto.builder()
                .userId(1)
                .name("John")
                .phoneNumber("0637490343")
                .build();
    }

    public static CustomerResponseDto mockCustomerResponseDto() {
        return CustomerResponseDto.builder()
                .id(2)
                .userId(1)
                .name("John")
                .phoneNumber("0637490343")
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static CustomerUpdateDto mockCustomerUpdateDto() {
        return CustomerUpdateDto.builder()
                .name("Johnathan")
                .phoneNumber("0637490344")
                .build();
    }
}
