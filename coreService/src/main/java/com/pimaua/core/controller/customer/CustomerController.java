package com.pimaua.core.controller.customer;

import com.pimaua.core.dto.ResponseDto;
import com.pimaua.core.dto.customer.CustomerCreateDto;
import com.pimaua.core.dto.customer.CustomerResponseDto;
import com.pimaua.core.dto.customer.CustomerUpdateDto;
import com.pimaua.core.dto.enums.CustomerResponseType;
import com.pimaua.core.service.customer.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/customer", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class CustomerController {
    private CustomerService customerService;

    @PostMapping("/create")
    public ResponseEntity<ResponseDto<CustomerResponseDto>> createCustomer(@Valid @RequestBody CustomerCreateDto customerCreateDto) {
        CustomerResponseDto customerResponseDto = customerService.createCustomer(customerCreateDto);
        CustomerResponseType type = CustomerResponseType.CREATED;
        ResponseDto<CustomerResponseDto> response = new ResponseDto<>(type.getStatusCode(), type.getMessage(), customerResponseDto);
        return ResponseEntity.status(type.getStatusCode())
                .body(response);
    }

    @GetMapping("/fetch")
    public ResponseEntity<ResponseDto<List<CustomerResponseDto>>> findAllCustomers() {
        List<CustomerResponseDto> customers = customerService.findAll();
        CustomerResponseType type = CustomerResponseType.SUCCESS;
        ResponseDto<List<CustomerResponseDto>> response = new ResponseDto<>(type.getStatusCode(), type.getMessage(), customers);
        return ResponseEntity.status(type.getStatusCode())
                .body(response);
    }

    @GetMapping("/fetch/{id}")
    public ResponseEntity<ResponseDto<CustomerResponseDto>> findCustomer(@PathVariable Integer id) {
        CustomerResponseDto customer = customerService.findById(id);
        CustomerResponseType type = CustomerResponseType.SUCCESS;
        ResponseDto<CustomerResponseDto> response = new ResponseDto<>(type.getStatusCode(), type.getMessage(), customer);
        return ResponseEntity.status(type.getStatusCode())
                .body(response);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseDto<CustomerResponseDto>> updateCustomer
            (@PathVariable Integer id, @Valid @RequestBody CustomerUpdateDto customerUpdateDto) {
        CustomerResponseDto customerResponseDto = customerService.updateCustomer(id, customerUpdateDto);
        CustomerResponseType type = CustomerResponseType.SUCCESS;
        ResponseDto<CustomerResponseDto> response = new ResponseDto<>(type.getStatusCode(), type.getMessage(), customerResponseDto);
        return ResponseEntity.status(type.getStatusCode())
                .body(response);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<ResponseDto<CustomerResponseDto>> deleteCustomer(@PathVariable Integer id) {
        customerService.deleteCustomer(id);
        CustomerResponseType type = CustomerResponseType.SUCCESS;
        ResponseDto<CustomerResponseDto> response = new ResponseDto<>(type.getStatusCode(), type.getMessage(), null);
        return ResponseEntity.status(type.getStatusCode())
                .body(response);
    }
}
