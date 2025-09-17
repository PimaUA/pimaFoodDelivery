package com.pimaua.core.service.customer;

import com.pimaua.core.dto.customer.CustomerCreateDto;
import com.pimaua.core.dto.customer.CustomerResponseDto;
import com.pimaua.core.dto.customer.CustomerUpdateDto;
import com.pimaua.core.entity.customer.Customer;
import com.pimaua.core.exception.custom.notfound.CustomerNotFoundException;
import com.pimaua.core.mapper.customer.CustomerMapper;
import com.pimaua.core.repository.customer.CustomerRepository;
import com.pimaua.core.service.customer.testdata.CustomerTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
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
        mockCustomer = CustomerTestData.mockCustomer();
        mockCustomerCreateDto = CustomerTestData.mockCustomerCreateDto();
        mockCustomerResponseDto = CustomerTestData.mockCustomerResponseDto();
        mockCustomerUpdateDto = CustomerTestData.mockCustomerUpdateDto();
    }

    //CreateCustomerTests
    @Test
    void createCustomer_Success() {
        // Given: a valid CustomerCreateDto and mocked repository/mapper behavior
        when(customerMapper.toEntity(mockCustomerCreateDto)).thenReturn(mockCustomer);
        when(customerRepository.save(mockCustomer)).thenReturn(mockCustomer);
        when(customerMapper.toResponseDto(mockCustomer)).thenReturn(mockCustomerResponseDto);

        // When: createCustomer is called
        CustomerResponseDto result = customerService.createCustomer(mockCustomerCreateDto);

        // Then: result is not null, matches expected DTO, and dependencies are called
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
        // Given: a valid CustomerCreateDto, but repository save will throw exception
        when(customerMapper.toEntity(mockCustomerCreateDto)).thenReturn(mockCustomer);
        when(customerRepository.save(mockCustomer)).thenThrow(new RuntimeException("Database error"));

        // When & Then: exception is propagated
        assertThrows(RuntimeException.class, () -> {
            customerService.createCustomer(mockCustomerCreateDto);
        });
    }

    //find all tests
    @Test
    void findAll_Success() {
        // Given: repository has one customer and mapper returns corresponding DTO
        List<Customer> customers = Arrays.asList(mockCustomer);
        List<CustomerResponseDto> responseDtos = Arrays.asList(mockCustomerResponseDto);
        when(customerRepository.findAll()).thenReturn(customers);
        when(customerMapper.toListDto(customers)).thenReturn(responseDtos);

        // When: findAll is called
        List<CustomerResponseDto> result = customerService.findAll();

        // Then: list is returned correctly
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(responseDtos, result);
    }

    @Test
    void findAll_EmptyList() {
        /// Given: repository returns empty list
        List<Customer> emptyCustomers = List.of();
        List<CustomerResponseDto> emptyDtos = List.of();
        when(customerRepository.findAll()).thenReturn(emptyCustomers);
        when(customerMapper.toListDto(emptyCustomers)).thenReturn(emptyDtos);

        // When: findAll is called
        List<CustomerResponseDto> result = customerService.findAll();

        // Then: result is empty list
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    //findById tests
    @Test
    void findById_Success() {
        // Given: a customer exists for ID 2
        when(customerRepository.findById(2)).thenReturn(Optional.of(mockCustomer));
        when(customerMapper.toResponseDto(mockCustomer)).thenReturn(mockCustomerResponseDto);

        // When: findById is called with ID 2
        CustomerResponseDto result = customerService.findById(2);

        // Then: correct DTO is returned
        assertNotNull(result);
        assertEquals(mockCustomerResponseDto, result);
    }

    @Test
    void findById_CustomerNotFound() {
        // Given: no customer exists for ID 999
        when(customerRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then: CustomerNotFoundException is thrown
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
            customerService.findById(999);
        });

        assertEquals("Customer not found with ID 999", exception.getMessage());
    }

    //findByNameTests
    @Test
    void findByName_Success() {
        // Given: a customer exists with name "John"
        when(customerRepository.findByName("John")).thenReturn(Optional.of(mockCustomer));
        when(customerMapper.toResponseDto(mockCustomer)).thenReturn(mockCustomerResponseDto);

        // When: findByName is called
        CustomerResponseDto result = customerService.findByName("John");

        // Then: correct DTO is returned
        assertNotNull(result);
        assertEquals(mockCustomerResponseDto, result);
    }

    @Test
    void findByName_CustomerNotFound() {
        // Given: no customer exists with name "David"
        when(customerRepository.findByName(anyString())).thenReturn(Optional.empty());

        // When & Then: CustomerNotFoundException is thrown
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class,
                () -> {
                    customerService.findByName("David");
                });
        assertEquals("Customer not found with name David", exception.getMessage());
    }

    //update Customer tests
    @Test
    void updateCustomer_Success() {
        // Given: a customer exists for ID 1, and valid update DTO
        when(customerRepository.findById(1)).thenReturn(Optional.of(mockCustomer));
        when(customerRepository.save(mockCustomer)).thenReturn(mockCustomer);
        when(customerMapper.toResponseDto(mockCustomer)).thenReturn(mockCustomerResponseDto);

        // When: updateCustomer is called
        CustomerResponseDto result = customerService.updateCustomer(1, mockCustomerUpdateDto);

        // Then: result matches expected DTO, repository and mapper are called
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
        // Given: customer does not exist for ID 999
        when(customerRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then: exception is thrown
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
            customerService.updateCustomer(999, mockCustomerUpdateDto);
        });

        assertEquals("Customer not found", exception.getMessage());
    }

    @Test
    void updateCustomer_RepositoryException() {
        // Given: customer exists, but save operation will fail
        when(customerRepository.findById(1)).thenReturn(Optional.of(mockCustomer));
        when(customerRepository.save(mockCustomer)).thenThrow(new RuntimeException("Database error"));

        // Given: customer exists, but save operation will fail
        assertThrows(RuntimeException.class, () -> {
            customerService.updateCustomer(1, mockCustomerUpdateDto);
        });
    }

    //delete Customer tests
    @Test
    void deleteCustomer_Success() {
        // Given: customer exists for ID 1
        when(customerRepository.findById(1)).thenReturn(Optional.of(mockCustomer));

        // When: delete is called
        customerService.delete(1);

        // Then: repository delete method is called
        verify(customerRepository).findById(1);
        verify(customerRepository).delete(mockCustomer);
    }

    @Test
    void deleteCustomer_CustomerNotFound() {
        // Given: customer does not exist for ID 999
        when(customerRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When & Then: exception is thrown, delete is not called
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class, () -> {
            customerService.delete(999);
        });

        assertEquals("Customer not found with ID 999", exception.getMessage());
        verify(customerRepository).findById(999);
        verify(customerRepository, never()).delete(any());
    }

    @Test
    void deleteCustomer_RepositoryException() {
        // Given: customer exists, but delete operation will fail
        when(customerRepository.findById(1)).thenReturn(Optional.of(mockCustomer));
        doThrow(new RuntimeException("Database error")).when(customerRepository).delete(mockCustomer);

        // When & Then: exception is propagated
        assertThrows(RuntimeException.class, () -> {
            customerService.delete(1);
        });

        verify(customerRepository).findById(1);
        verify(customerRepository).delete(mockCustomer);
    }

    // Validation / edge cases
    @Test
    void findById_InvalidId_ThrowsIllegalArgumentException() {
        // When & Then: invalid ID throws exception
        assertThrows(IllegalArgumentException.class, () -> customerService.findById(0));
    }

    @Test
    void updateCustomer_NullUpdateDto_ThrowsIllegalArgumentException() {
        // When & Then: null update DTO throws exception
        assertThrows(IllegalArgumentException.class, () -> customerService.updateCustomer(1, null));
    }

    @Test
    void createCustomer_NullInput_ThrowsIllegalArgumentException() {
        // When & Then: null create DTO throws exception
        assertThrows(IllegalArgumentException.class, () -> customerService.createCustomer(null));
    }

    @Test
    void findByName_Null_ThrowsIllegalArgumentException() {
        // When & Then: null name throws exception
        assertThrows(IllegalArgumentException.class, () -> customerService.findByName(null));
    }
}
