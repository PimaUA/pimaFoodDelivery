package com.pimaua.core.service.customer;

import com.pimaua.core.dto.customer.CustomerCreateDto;
import com.pimaua.core.dto.customer.CustomerResponseDto;
import com.pimaua.core.dto.customer.CustomerUpdateDto;
import com.pimaua.core.entity.customer.Customer;
import com.pimaua.core.exception.custom.notfound.CustomerNotFoundException;
import com.pimaua.core.mapper.customer.CustomerMapper;
import com.pimaua.core.repository.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    private Customer mockCustomer;
    private CustomerCreateDto mockCustomerCreateDto;
    private CustomerResponseDto mockCustomerResponseDto;
    private CustomerUpdateDto mockCustomerUpdateDto;

    @BeforeEach
    void setup() {
        mockCustomer = Customer.builder()
                .id(2)
                .userId(1)
                .name("John")
                .phoneNumber("0637490343")
                .updatedAt(LocalDateTime.now())
                .build();

        mockCustomerCreateDto = CustomerCreateDto.builder()
                .userId(1)
                .name("John")
                .phoneNumber("0637490343")
                .build();

        mockCustomerResponseDto = CustomerResponseDto.builder()
                .id(2)
                .userId(1)
                .name("John")
                .phoneNumber("0637490343")
                .updatedAt(LocalDateTime.now())
                .build();

        mockCustomerUpdateDto = CustomerUpdateDto.builder()
                .name("Johnathan")
                .phoneNumber("0637490344")
                .build();
    }

    //CreateCustomerTests
    @Test
    void createCustomer_Success() {
        //given
        when(customerMapper.toEntity(mockCustomerCreateDto)).thenReturn(mockCustomer);
        when(customerRepository.save(mockCustomer)).thenReturn(mockCustomer);
        when(customerMapper.toResponseDto(mockCustomer)).thenReturn(mockCustomerResponseDto);
        //when
        CustomerResponseDto result = customerService.createCustomer(mockCustomerCreateDto);
        //then
        assertNotNull(result);
        assertEquals(mockCustomerResponseDto.getId(), result.getId());
        assertEquals(mockCustomerResponseDto.getName(), result.getName());

        // interaction verification
        verify(customerMapper).toEntity(mockCustomerCreateDto);
        verify(customerRepository).save(mockCustomer);
        verify(customerMapper).toResponseDto(mockCustomer);
    }

    @Test
    void createCustomer_RepositoryException() {
        //given
        when(customerMapper.toEntity(mockCustomerCreateDto)).thenReturn(mockCustomer);
        when(customerRepository.save(mockCustomer)).thenThrow(new RuntimeException("Database error"));
        //when&then
        assertThrows(RuntimeException.class, () -> {
            customerService.createCustomer(mockCustomerCreateDto);
        });
    }

    //find all tests
    @Test
    void findAll_Success() {
        //given
        List<Customer> customers = Arrays.asList(mockCustomer);
        List<CustomerResponseDto> responseDtos = Arrays.asList(mockCustomerResponseDto);
        when(customerRepository.findAll()).thenReturn(customers);
        when(customerMapper.toListDto(customers)).thenReturn(responseDtos);
        //when
        List<CustomerResponseDto> result = customerService.findAll();
        //then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(responseDtos, result);
    }

    @Test
    void findAll_EmptyList() {
        // Given
        List<Customer> emptyCustomers = List.of();
        List<CustomerResponseDto> emptyDtos = List.of();
        when(customerRepository.findAll()).thenReturn(emptyCustomers);
        when(customerMapper.toListDto(emptyCustomers)).thenReturn(emptyDtos);
        // When
        List<CustomerResponseDto> result = customerService.findAll();
        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    //findById tests
    @Test
    void findById_Success() {
        //given
        when(customerRepository.findById(2)).thenReturn(Optional.of(mockCustomer));
        when(customerMapper.toResponseDto(mockCustomer)).thenReturn(mockCustomerResponseDto);
        //when
        CustomerResponseDto result = customerService.findById(2);
        //then
        assertNotNull(result);
        assertEquals(mockCustomerResponseDto, result);
    }

    @Test
    void findById_CustomerNotFound() {
        //given
        when(customerRepository.findById(anyInt())).thenReturn(Optional.empty());
        //when&then
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
            customerService.findById(999);
        });
        assertEquals("Customer not found with ID 999", exception.getMessage());
    }

    //findByNameTests
    @Test
    void findByName_Success() {
        //given
        when(customerRepository.findByName("John")).thenReturn(Optional.of(mockCustomer));
        when(customerMapper.toResponseDto(mockCustomer)).thenReturn(mockCustomerResponseDto);
        //when
        CustomerResponseDto result = customerService.findByName("John");

        //then
        assertNotNull(result);
        assertEquals(mockCustomerResponseDto, result);
    }

    @Test
    void findByName_CustomerNotFound() {
        //given
        when(customerRepository.findByName(anyString())).thenReturn(Optional.empty());
        //when&then
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class,
                () -> {
                    customerService.findByName("David");
                });
        assertEquals("Customer not found with name David", exception.getMessage());
    }

    //update Customer tests
    @Test
    void updateCustomer_Success() {
        // Given
        when(customerRepository.findById(1)).thenReturn(Optional.of(mockCustomer));
        when(customerRepository.save(mockCustomer)).thenReturn(mockCustomer);
        when(customerMapper.toResponseDto(mockCustomer)).thenReturn(mockCustomerResponseDto);

        // When
        CustomerResponseDto result = customerService.updateCustomer(1, mockCustomerUpdateDto);

        // Then
        assertNotNull(result);
        assertEquals(mockCustomerResponseDto, result);

        // interaction verification
        verify(customerRepository).findById(1);
        verify(customerRepository).save(mockCustomer);
        verify(customerMapper).toResponseDto(mockCustomer);
        verify(customerMapper).updateEntity(mockCustomer, mockCustomerUpdateDto);
    }

    @Test
    void updateCustomer_CustomerNotFound() {
        // Given
        when(customerRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
            customerService.updateCustomer(999, mockCustomerUpdateDto);
        });

        assertEquals("Customer not found", exception.getMessage());
    }

    @Test
    void updateCustomer_RepositoryException() {
        // Given
        when(customerRepository.findById(1)).thenReturn(Optional.of(mockCustomer));
        when(customerRepository.save(mockCustomer)).thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            customerService.updateCustomer(1, mockCustomerUpdateDto);
        });
    }

    //delete Customer tests
    @Test
    void deleteCustomer_Success() {
        // Given
        when(customerRepository.findById(1)).thenReturn(Optional.of(mockCustomer));

        // When
        customerService.delete(1);

        // Then
        verify(customerRepository).findById(1);
        verify(customerRepository).delete(mockCustomer);
    }

    @Test
    void deleteCustomer_CustomerNotFound() {
        // Given
        when(customerRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
            customerService.delete(999);
        });

        assertEquals("Customer not found with ID 999", exception.getMessage());
        verify(customerRepository).findById(999);
        verify(customerRepository, never()).delete(any());
    }

    @Test
    void deleteCustomer_RepositoryException() {
        // Given
        when(customerRepository.findById(1)).thenReturn(Optional.of(mockCustomer));
        doThrow(new RuntimeException("Database error")).when(customerRepository).delete(mockCustomer);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            customerService.delete(1);
        });

        verify(customerRepository).findById(1);
        verify(customerRepository).delete(mockCustomer);
    }

    @Test
    void findById_InvalidId_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> customerService.findById(0));
    }

    @Test
    void updateCustomer_NullUpdateDto_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> customerService.updateCustomer(1, null));
    }

    //edge cases
    @Test
    void createCustomer_NullInput_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> customerService.createCustomer(null));
    }

    @Test
    void findByName_Null_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> customerService.findByName(null));
    }
}
