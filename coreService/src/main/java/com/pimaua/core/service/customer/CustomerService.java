package com.pimaua.core.service.customer;

import com.pimaua.core.dto.customer.CustomerCreateDto;
import com.pimaua.core.dto.customer.CustomerResponseDto;
import com.pimaua.core.dto.customer.CustomerUpdateDto;
import com.pimaua.core.entity.customer.Customer;
import com.pimaua.core.exception.custom.notfound.CustomerNotFoundException;
import com.pimaua.core.mapper.customer.CustomerMapper;
import com.pimaua.core.repository.customer.CustomerRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerResponseDto createCustomer(CustomerCreateDto customerCreateDto) {
        if (customerCreateDto == null) {
            throw new IllegalArgumentException("CustomerCreateDto cannot be null");
        }
        Customer customer = customerMapper.toEntity(customerCreateDto);
        customer = customerRepository.save(customer);
        return customerMapper.toResponseDto(customer);
    }

    @Transactional(readOnly = true)
    public List<CustomerResponseDto> findAll() {
        return customerMapper.toListDto(customerRepository.findAll());
    }

    @Transactional(readOnly = true)
    public CustomerResponseDto findById(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid customer ID");
        }
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID " + id));
        return customerMapper.toResponseDto(customer);
    }

    @Transactional(readOnly = true)
    public CustomerResponseDto findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        Customer customer = customerRepository.findByName(name)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with name " + name));
        return customerMapper.toResponseDto(customer);
    }

    public CustomerResponseDto updateCustomer(Integer id, CustomerUpdateDto customerUpdateDto) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Invalid customer ID");
        }
        if (customerUpdateDto == null) {
            throw new IllegalArgumentException("CustomerUpdateDto cannot be null");
        }
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        customerMapper.updateEntity(customer, customerUpdateDto);
        Customer savedCustomer=customerRepository.save(customer);
        return customerMapper.toResponseDto(savedCustomer);
    }

    public void deleteCustomer(Integer id) {
        Customer customer=customerRepository.findById(id)
                .orElseThrow(()->new CustomerNotFoundException("Customer not found with ID " + id));
        customerRepository.delete(customer);
    }
}
