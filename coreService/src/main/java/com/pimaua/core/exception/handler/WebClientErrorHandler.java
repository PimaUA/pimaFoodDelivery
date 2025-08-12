package com.pimaua.core.exception.handler;

import com.pimaua.core.dto.ErrorResponseDto;
import com.pimaua.core.exception.custom.CustomWebClientFallBackException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;

import java.time.Instant;

public class WebClientErrorHandler {
    private static final Logger logger = LoggerFactory.getLogger(WebClientErrorHandler.class);

    public ExchangeFilterFunction filterError() {
        return ExchangeFilterFunction.ofResponseProcessor(response -> {
            if (response.statusCode().isError()) {
                return
                        handleError(response);
            }
            return Mono.just(response);
        });
    }

    private Mono<ClientResponse> handleError(ClientResponse response) {
        return
                response.bodyToMono(ErrorResponseDto.class)
                        .defaultIfEmpty(new ErrorResponseDto())

                        .flatMap(responseDto -> {
                            HttpStatusCode statusCode = response.statusCode();
                            String errorMessage = String.format("HTTP %d %s", statusCode.value(), responseDto.getErrorMessage());

                            logger.error("An error occurred during WebClient call. Status: {}, Message: {}", statusCode, errorMessage);

                            ErrorResponseDto fallbackResponse =
                                    ErrorResponseDto.builder()
                                            .errorCode(statusCode)
                                            .errorMessage(errorMessage)
                                            .errorTimestamp(Instant.now())
                                            .build();

                            return Mono.error(new CustomWebClientFallBackException(fallbackResponse));
                        });
    }
}
