package com.pimaua.payment.exception.errordecoder;

import com.pimaua.payment.exception.custom.OrderNotFoundException;
import com.pimaua.payment.exception.custom.PaymentProcessingException;
import com.pimaua.payment.exception.custom.ServiceClientException;
import com.pimaua.payment.exception.custom.ServiceUnavailableException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class OrderServiceClientDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 400 -> new PaymentProcessingException("Failed to process payment");
            case 404 -> new OrderNotFoundException("Order not found");
            case 503 -> new ServiceUnavailableException("Service unavailable, please retry later");
            default -> new ServiceClientException(
                    "Unexpected error: " + response.status(),
                    response.status()
            );
        };
    }
}
