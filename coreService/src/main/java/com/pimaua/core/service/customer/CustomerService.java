package com.pimaua.core.service.customer;

import com.pimaua.core.dto.customer.CustomerCreateDto;
import com.pimaua.core.dto.customer.CustomerResponseDto;
import com.pimaua.core.dto.customer.CustomerUpdateDto;
import com.pimaua.core.entity.customer.Customer;
import com.pimaua.core.exception.custom.notfound.CustomerNotFoundException;
import com.pimaua.core.mapper.customer.CustomerMapper;
import com.pimaua.core.repository.customer.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    public CustomerResponseDto createCustomer(CustomerCreateDto customerCreateDto) {
        if (customerCreateDto == null) {
            logger.error("Failed to create customer, no input");
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
            logger.error("Failed to find customer, invalid or missing Id");
            throw new IllegalArgumentException("Invalid customer ID");
        }
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Customer not found with id={}", id);
                    return new CustomerNotFoundException("Customer not found with ID " + id);
                });
        return customerMapper.toResponseDto(customer);
    }

    @Transactional(readOnly = true)
    public CustomerResponseDto findByName(String name) {
        if (name == null || name.isBlank()) {
            logger.error("Failed to find customer, name missing");
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        Customer customer = customerRepository.findByName(name)
                .orElseThrow(() -> {
                    logger.error("Customer not found with name={}", name);
                    return new CustomerNotFoundException("Customer not found with name " + name);
                });
        return customerMapper.toResponseDto(customer);
    }

    public CustomerResponseDto updateCustomer(Integer id, CustomerUpdateDto customerUpdateDto) {
        if (id == null || id <= 0) {
            logger.error("Failed to update customer, invalid or missing Id");
            throw new IllegalArgumentException("Invalid customer ID");
        }
        if (customerUpdateDto == null) {
            logger.error("Failed to update customer, no input");
            throw new IllegalArgumentException("CustomerUpdateDto cannot be null");
        }
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Customer not found with id={}", id);
                    return new CustomerNotFoundException("Customer not found");
                });
        customerMapper.updateEntity(customer, customerUpdateDto);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponseDto(savedCustomer);
    }

    public void delete(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Customer not found with id={}", id);
                    return new CustomerNotFoundException("Customer not found with ID " + id);
                });
        customerRepository.delete(customer);
    }
}
