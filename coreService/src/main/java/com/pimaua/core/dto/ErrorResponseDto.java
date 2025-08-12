package com.pimaua.core.dto;

import lombok.*;
import org.springframework.http.HttpStatusCode;

import java.time.Instant;

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
}
