package com.pimaua.payment.exception.custom;

import lombok.Getter;

@Getter
public class ServiceClientException extends RuntimeException{
    private final int status;

    public ServiceClientException(String message) {
        super(message);
        this.status = -1; // unknown
    }

    public ServiceClientException(String message, int status) {
        super(message);
        this.status = status;
    }
}
