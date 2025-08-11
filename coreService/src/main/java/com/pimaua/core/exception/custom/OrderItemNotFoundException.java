package com.pimaua.core.exception.custom;

public class OrderItemNotFoundException extends RuntimeException{

    public OrderItemNotFoundException(String message){
        super(message);
    }
}
