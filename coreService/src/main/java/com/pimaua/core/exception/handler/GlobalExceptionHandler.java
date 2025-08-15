package com.pimaua.core.exception.handler;

import com.pimaua.core.dto.ErrorResponseDto;;
import com.pimaua.core.exception.custom.notfound.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({CustomerNotFoundException.class, MenuNotFoundException.class, OpeningHoursNotFoundException.class,
    OpeningHoursNotFoundException.class, OrderItemNotFoundException.class, OrderNotFoundException.class,
    RestaurantNotFoundException.class})
    public ResponseEntity<ErrorResponseDto> handleNotFoundExceptions
            (CustomerNotFoundException customerNotFoundException, WebRequest webRequest) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .path(webRequest.getContextPath())
                .errorCode(HttpStatus.NOT_FOUND)
                .errorMessage(customerNotFoundException.getMessage())
                .errorTimestamp(Instant.now())
                .build();
        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto>
    handleBadRequest(IllegalArgumentException illegalArgumentException,WebRequest webRequest) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .path(webRequest.getContextPath())
                .errorCode(HttpStatus.BAD_REQUEST)
                .errorMessage(illegalArgumentException.getMessage())
                .errorTimestamp(Instant.now())
                .build();
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception exception, WebRequest webRequest) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .path(webRequest.getContextPath())
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .errorMessage(exception.getMessage())
                .errorTimestamp(Instant.now())
                .build();
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
