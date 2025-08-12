package com.pimaua.core.exception.custom.notfound;

public class OrderItemNotFoundException extends RuntimeException{

    public OrderItemNotFoundException(String message){
        super(message);
    }
}
