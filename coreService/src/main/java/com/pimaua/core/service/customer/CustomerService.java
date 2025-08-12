package com.pimaua.core.service.customer;

import com.pimaua.core.dto.customer.CustomerCreateDto;
import com.pimaua.core.dto.customer.CustomerResponseDto;
import com.pimaua.core.dto.customer.CustomerUpdateDto;
import com.pimaua.core.entity.customer.Customer;
import com.pimaua.core.exception.custom.notfound.CustomerNotFoundException;
import com.pimaua.core.mapper.customer.CustomerMapper;
import com.pimaua.core.repository.customer.CustomerRepository;
import jakarta.transaction.Transactional;
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
        Customer customer = customerMapper.toEntity(customerCreateDto);
        customer = customerRepository.save(customer);
        return customerMapper.toResponseDto(customer);
    }

    public List<CustomerResponseDto> findAll() {
        return customerMapper.toListDto(customerRepository.findAll());
    }

    public CustomerResponseDto findById(Integer id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID " + id));
        return customerMapper.toResponseDto(customer);
    }

    public CustomerResponseDto findByName(String name) {
        Customer customer = customerRepository.findByName(name)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with name " + name));
        return customerMapper.toResponseDto(customer);
    }

    public CustomerResponseDto updateCustomer(Integer id, CustomerUpdateDto customerUpdateDto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        customerMapper.updateEntity(customer, customerUpdateDto);
        Customer savedCustomer=customerRepository.save(customer);
        return customerMapper.toResponseDto(savedCustomer);
    }

    public void deleteCustomer(Integer id) {
        if (!customerRepository.existsById(id)) {
            throw new CustomerNotFoundException("Customer not found with ID: " + id);
        }
        customerRepository.deleteById(id);
    }
}
