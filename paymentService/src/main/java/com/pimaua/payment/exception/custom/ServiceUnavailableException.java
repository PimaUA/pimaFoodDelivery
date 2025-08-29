package com.pimaua.payment.exception.custom;

public class ServiceUnavailableException extends ServiceClientException {
    public ServiceUnavailableException(String message) {
        super(message, 503);
    }
}
