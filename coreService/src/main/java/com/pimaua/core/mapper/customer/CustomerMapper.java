package com.pimaua.core.mapper.customer;

import com.pimaua.core.dto.customer.CustomerCreateDto;
import com.pimaua.core.dto.customer.CustomerResponseDto;
import com.pimaua.core.dto.customer.CustomerUpdateDto;
import com.pimaua.core.entity.customer.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {
    CustomerResponseDto toResponseDto(Customer customer);

    Customer toEntity(CustomerCreateDto customerCreateDto);

    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget Customer existingCustomer, CustomerUpdateDto customerUpdateDto);

    List<CustomerResponseDto> toListDto(List<Customer> customers);
}
