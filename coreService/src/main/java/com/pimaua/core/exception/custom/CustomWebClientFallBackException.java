package com.pimaua.core.exception.custom;

import com.pimaua.core.dto.ErrorResponseDto;
import lombok.Getter;

@Getter
public class CustomWebClientFallBackException extends RuntimeException{
    private final ErrorResponseDto fallback;

    public CustomWebClientFallBackException(ErrorResponseDto fallback) {
        super(fallback.getErrorMessage());
        this.fallback = fallback;
    }
}
