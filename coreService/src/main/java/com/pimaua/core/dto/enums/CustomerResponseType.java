package com.pimaua.core.dto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum CustomerResponseType {
    CREATED(HttpStatus.CREATED, "Customer created successfully"),
    SUCCESS(HttpStatus.OK, "Request processed successfully"),
    UPDATE_FAILED(HttpStatus.EXPECTATION_FAILED, "Update operation failed."),
    DELETE_FAILED(HttpStatus.EXPECTATION_FAILED, "Delete operation failed."),
    ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred. Please try again later"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Customer not found");

    private final HttpStatusCode statusCode;
    private final String message;
}
