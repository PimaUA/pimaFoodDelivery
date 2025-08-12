package com.pimaua.core.config;

import com.pimaua.core.exception.handler.WebClientErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {
    private final WebClientErrorHandler webClientErrorHandler;

    @Bean("menuItemWebClient")
    public WebClient menuItemWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://api.taiga.io/api/v1") //???????
                .filter(webClientErrorHandler.filterError())
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

}
