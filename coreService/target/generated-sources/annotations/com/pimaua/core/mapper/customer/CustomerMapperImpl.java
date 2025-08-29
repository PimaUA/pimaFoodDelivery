package com.pimaua.core.mapper.customer;

import com.pimaua.core.dto.customer.CustomerCreateDto;
import com.pimaua.core.dto.customer.CustomerResponseDto;
import com.pimaua.core.dto.customer.CustomerUpdateDto;
import com.pimaua.core.entity.customer.Customer;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-29T12:41:27+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.10 (Oracle Corporation)"
)
@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public CustomerResponseDto toResponseDto(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        CustomerResponseDto.CustomerResponseDtoBuilder customerResponseDto = CustomerResponseDto.builder();

        customerResponseDto.id( customer.getId() );
        customerResponseDto.userId( customer.getUserId() );
        customerResponseDto.name( customer.getName() );
        customerResponseDto.phoneNumber( customer.getPhoneNumber() );
        customerResponseDto.updatedAt( customer.getUpdatedAt() );

        return customerResponseDto.build();
    }

    @Override
    public Customer toEntity(CustomerCreateDto customerCreateDto) {
        if ( customerCreateDto == null ) {
            return null;
        }

        Customer.CustomerBuilder customer = Customer.builder();

        customer.userId( customerCreateDto.getUserId() );
        customer.name( customerCreateDto.getName() );
        customer.phoneNumber( customerCreateDto.getPhoneNumber() );

        return customer.build();
    }

    @Override
    public void updateEntity(Customer existingCustomer, CustomerUpdateDto customerUpdateDto) {
        if ( customerUpdateDto == null ) {
            return;
        }

        existingCustomer.setName( customerUpdateDto.getName() );
        existingCustomer.setPhoneNumber( customerUpdateDto.getPhoneNumber() );
    }

    @Override
    public List<CustomerResponseDto> toListDto(List<Customer> customers) {
        if ( customers == null ) {
            return null;
        }

        List<CustomerResponseDto> list = new ArrayList<CustomerResponseDto>( customers.size() );
        for ( Customer customer : customers ) {
            list.add( toResponseDto( customer ) );
        }

        return list;
    }
}
