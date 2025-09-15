package com.pimaua.core.utils;

import com.pimaua.core.dto.ResponseDto;
import com.pimaua.core.dto.enums.EntityType;
import com.pimaua.core.dto.enums.ResponseType;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder {

    private ResponseBuilder() {
    }

    public static <T> ResponseEntity<ResponseDto<T>> buildResponse(ResponseType type, EntityType entityType, T data) {
        String message = type.formatMessage(entityType.getDisplayName());
        ResponseDto<T> response = new ResponseDto<>(type.getStatusCode(), message, data);
        return ResponseEntity.status(type.getStatusCode()).body(response);
    }
}
