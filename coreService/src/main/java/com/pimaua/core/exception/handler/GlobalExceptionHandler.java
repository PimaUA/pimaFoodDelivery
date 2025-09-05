package com.pimaua.core.exception.handler;

import com.pimaua.core.dto.ErrorResponseDto;
import com.pimaua.core.dto.FieldErrorDto;
import com.pimaua.core.exception.ActiveMenuConflictException;
import com.pimaua.core.exception.InvalidOrderStatusTransitionException;
import com.pimaua.core.exception.NotUpdatedOrderStatusException;
import com.pimaua.core.exception.OrderDeletionNotAllowedException;
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
import java.util.Collections;
import java.util.List;

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
                .fieldErrors(Collections.emptyList())
                .errorTimestamp(Instant.now())
                .build();
        return new ResponseEntity<>(errorResponseDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ErrorResponseDto>
    handleBadRequest(RuntimeException ex, WebRequest webRequest) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .path(webRequest.getDescription(false).replace("uri=", ""))
                .errorCode(HttpStatus.BAD_REQUEST)
                .errorMessage(ex.getMessage())
                .fieldErrors(Collections.emptyList())
                .errorTimestamp(Instant.now())
                .build();
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest webRequest) {

        List<FieldErrorDto> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldErrorDto(error.getField(), error.getDefaultMessage()))
                .toList();

        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .path(webRequest.getDescription(false).replace("uri=", ""))
                .errorCode(HttpStatus.BAD_REQUEST)
                .errorMessage("Validation failed")
                .fieldErrors(fieldErrors)
                .errorTimestamp(Instant.now())
                .build();
        return new ResponseEntity<>(errorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateKey(
            DataIntegrityViolationException ex, WebRequest webRequest) {
        String message = "Data integrity violation";
        if (ex.getCause() instanceof ConstraintViolationException cve) {
            String constraintName = cve.getConstraintName();
            if (constraintName != null) {
                if (constraintName.contains("phone")) {
                    message = "Phone number already exists";
                } else if (constraintName.contains("opening_hours")) {
                    message = "Opening hours for this day already exist";
                }
            }
        }

        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .path(webRequest.getDescription(false).replace("uri=", ""))
                .errorCode(HttpStatus.CONFLICT)
                .errorMessage(message)
                .fieldErrors(Collections.emptyList())
                .errorTimestamp(Instant.now())
                .build();
        return new ResponseEntity<>(errorResponseDto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ActiveMenuConflictException.class, InvalidOrderStatusTransitionException.class,
            NotUpdatedOrderStatusException.class, OrderDeletionNotAllowedException.class})
    public ResponseEntity<ErrorResponseDto> handleDeleteWithActiveMenu
            (RuntimeException exception,
             WebRequest webRequest) {
        ErrorResponseDto errorResponseDto = ErrorResponseDto.builder()
                .path(webRequest.getDescription(false).replace("uri=", ""))
                .errorCode(HttpStatus.CONFLICT)
                .errorMessage(exception.getMessage())
                .fieldErrors(Collections.emptyList())
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
                .fieldErrors(Collections.emptyList())
                .errorTimestamp(Instant.now())
                .build();
        return new ResponseEntity<>(errorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
