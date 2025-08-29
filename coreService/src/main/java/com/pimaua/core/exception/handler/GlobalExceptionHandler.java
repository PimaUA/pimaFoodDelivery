package com.pimaua.core.exception.handler;

import com.pimaua.core.dto.ErrorResponseDto;
import com.pimaua.core.exception.custom.notfound.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.hibernate.exception.ConstraintViolationException;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CustomerNotFoundException.class, MenuNotFoundException.class, OpeningHoursNotFoundException.class,
    OpeningHoursNotFoundException.class, OrderItemNotFoundException.class, OrderNotFoundException.class,
    RestaurantNotFoundException.class})
    public ResponseEntity<ErrorResponseDto> handleNotFoundExceptions
            (Exception exception, WebRequest webRequest) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .path(webRequest.getDescription(false).replace("uri=", ""))
                .errorCode(HttpStatus.NOT_FOUND)
                .errorMessage(exception.getMessage())
                .errorTimestamp(Instant.now())
                .build();
        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({IllegalArgumentException.class,IllegalStateException.class})
    public ResponseEntity<ErrorResponseDto>
    handleBadRequest(IllegalArgumentException illegalArgumentException,WebRequest webRequest) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .path(webRequest.getDescription(false).replace("uri=", ""))
                .errorCode(HttpStatus.BAD_REQUEST)
                .errorMessage(illegalArgumentException.getMessage())
                .errorTimestamp(Instant.now())
                .build();
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest webRequest) {

        // Collect all field errors into a single message
        StringBuilder errorMessage = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errorMessage.append(error.getField())
                        .append(": ")
                        .append(error.getDefaultMessage())
                        .append("; ")
        );

        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .path(webRequest.getDescription(false).replace("uri=", ""))
                .errorCode(HttpStatus.BAD_REQUEST)
                .errorMessage(errorMessage.toString())
                .errorTimestamp(Instant.now())
                .build();

        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateKey(
            DataIntegrityViolationException ex, WebRequest webRequest) {
        String message = "Data integrity violation";
        if (ex.getCause() instanceof ConstraintViolationException) {
            message = "Phone number already exists";
        }

        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .path(webRequest.getDescription(false).replace("uri=", ""))
                .errorCode(HttpStatus.CONFLICT)
                .errorMessage(message)
                .errorTimestamp(Instant.now())
                .build();
        return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception exception, WebRequest webRequest) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .path(webRequest.getDescription(false).replace("uri=", ""))
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .errorMessage(exception.getMessage())
                .errorTimestamp(Instant.now())
                .build();
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
