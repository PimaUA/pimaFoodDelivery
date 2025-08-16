package com.pimaua.core.dto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum ResponseType {
    CREATED(HttpStatus.CREATED, "%s created successfully"),
    UPDATED(HttpStatus.OK, "%s updated successfully"),
    DELETED(HttpStatus.OK, "%s deleted successfully"),
    SUCCESS(HttpStatus.OK, "Request processed successfully"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "%s not found");

    private final HttpStatusCode statusCode;
    private final String message;

    public String formatMessage(String entity) {
        return String.format(message, entity);
    }
}
