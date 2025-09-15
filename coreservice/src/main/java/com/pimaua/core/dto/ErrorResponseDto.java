package com.pimaua.core.dto;

import lombok.*;
import org.springframework.http.HttpStatusCode;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponseDto {
    private  String path;
    private HttpStatusCode errorCode;
    private  String errorMessage;
    private Instant errorTimestamp;
    private List<FieldErrorDto> fieldErrors;
}
