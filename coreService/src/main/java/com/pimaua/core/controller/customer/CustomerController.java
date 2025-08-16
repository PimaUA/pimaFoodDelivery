package com.pimaua.core.controller.customer;

import com.pimaua.core.dto.ResponseDto;
import com.pimaua.core.dto.customer.CustomerCreateDto;
import com.pimaua.core.dto.customer.CustomerResponseDto;
import com.pimaua.core.dto.customer.CustomerUpdateDto;
import com.pimaua.core.dto.enums.EntityType;
import com.pimaua.core.dto.enums.ResponseType;
import com.pimaua.core.service.customer.CustomerService;
import com.pimaua.core.utils.ResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/customers", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<ResponseDto<CustomerResponseDto>> createCustomer(@Valid @RequestBody CustomerCreateDto customerCreateDto) {
        CustomerResponseDto customerResponseDto = customerService.createCustomer(customerCreateDto);
        return ResponseBuilder.buildResponse(ResponseType.CREATED, EntityType.CUSTOMER,customerResponseDto);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<CustomerResponseDto>>> findAllCustomers() {
        List<CustomerResponseDto> customers = customerService.findAll();
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS,EntityType.CUSTOMER,customers);
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseDto<CustomerResponseDto>> findCustomer(@PathVariable Integer id) {
        CustomerResponseDto customer = customerService.findById(id);
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS,EntityType.CUSTOMER,customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<CustomerResponseDto>> updateCustomer
            (@PathVariable Integer id, @Valid @RequestBody CustomerUpdateDto customerUpdateDto) {
        CustomerResponseDto customerResponseDto = customerService.updateCustomer(id, customerUpdateDto);
        return ResponseBuilder.buildResponse(ResponseType.UPDATED,EntityType.CUSTOMER,customerResponseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<CustomerResponseDto>> deleteCustomer(@PathVariable Integer id) {
        customerService.delete(id);
        return ResponseBuilder.buildResponse(ResponseType.DELETED,EntityType.CUSTOMER,null);
    }
}
