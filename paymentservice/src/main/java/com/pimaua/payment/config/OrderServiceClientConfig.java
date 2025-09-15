package com.pimaua.payment.config;

import com.pimaua.payment.exception.errordecoder.OrderServiceClientDecoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderServiceClientConfig {

    @Bean
    public ErrorDecoder feignErrorDecoder() {
        return new OrderServiceClientDecoder();
    }
}
