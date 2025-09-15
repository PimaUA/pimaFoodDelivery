package com.pimaua.core.controller.customer;

import com.pimaua.core.dto.ErrorResponseDto;
import com.pimaua.core.dto.ResponseDto;
import com.pimaua.core.dto.customer.CustomerCreateDto;
import com.pimaua.core.dto.customer.CustomerResponseDto;
import com.pimaua.core.dto.customer.CustomerUpdateDto;
import com.pimaua.core.dto.enums.EntityType;
import com.pimaua.core.dto.enums.ResponseType;
import com.pimaua.core.service.customer.CustomerService;
import com.pimaua.core.utils.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "CRUD REST APIs for Customer",
        description = "CRUD REST APIs for Customer inside CoreService for CREATE,UPDATE,FETCH,DELETE customer details"
)
@RestController
@RequestMapping(path = "/api/customers", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
public class CustomerController {
    private final CustomerService customerService;

    @Operation(
            summary = "Create Customer REST API",
            description = "REST API to create Customer inside CoreService"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "HTTP Status BAD REQUEST",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponseDto.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponseDto.class
                            )
                    )
            )
    }
    )
    @PostMapping
    public ResponseEntity<ResponseDto<CustomerResponseDto>> createCustomer(@Valid @RequestBody CustomerCreateDto customerCreateDto) {
        CustomerResponseDto customerResponseDto = customerService.createCustomer(customerCreateDto);
        return ResponseBuilder.buildResponse(ResponseType.CREATED, EntityType.CUSTOMER,customerResponseDto);
    }

    @Operation(
            summary = "Fetch Customer Details REST API",
            description = "REST API to fetch all Customers' details"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponseDto.class
                            )
                    )

            )
    }
    )
    @GetMapping
    public ResponseEntity<ResponseDto<List<CustomerResponseDto>>> findAllCustomers() {
        List<CustomerResponseDto> customers = customerService.findAll();
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS,EntityType.CUSTOMER,customers);
    }

    @Operation(
            summary = "Fetch specific Customer Details REST API",
            description = "REST API to fetch specific Customer details using Id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status NOT FOUND",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponseDto.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponseDto.class
                            )
                    )
            )
    }
    )
    @GetMapping("{id}")
    public ResponseEntity<ResponseDto<CustomerResponseDto>> findCustomer(@PathVariable Integer id) {
        CustomerResponseDto customer = customerService.findById(id);
        return ResponseBuilder.buildResponse(ResponseType.SUCCESS,EntityType.CUSTOMER,customer);
    }

    @Operation(
            summary = "Update specific Customer Details REST API",
            description = "REST API to update specific Customer details using Id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status NOT FOUND",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponseDto.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponseDto.class
                            )
                    )
            )
    }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<CustomerResponseDto>> updateCustomer
            (@PathVariable Integer id, @Valid @RequestBody CustomerUpdateDto customerUpdateDto) {
        CustomerResponseDto customerResponseDto = customerService.updateCustomer(id, customerUpdateDto);
        return ResponseBuilder.buildResponse(ResponseType.UPDATED,EntityType.CUSTOMER,customerResponseDto);
    }

    @Operation(
            summary = "Delete specific Customer Details REST API",
            description = "REST API to delete specific Customer details using Id"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status NOT FOUND",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponseDto.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(
                                    implementation = ErrorResponseDto.class
                            )
                    )
            )
    }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<CustomerResponseDto>> deleteCustomer(@PathVariable Integer id) {
        customerService.delete(id);
        return ResponseBuilder.buildResponse(ResponseType.DELETED,EntityType.CUSTOMER,null);
    }
}
