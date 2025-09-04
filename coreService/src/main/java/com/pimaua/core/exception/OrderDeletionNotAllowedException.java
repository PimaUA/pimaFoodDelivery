package com.pimaua.core.exception;

public class OrderDeletionNotAllowedException extends RuntimeException{

    public OrderDeletionNotAllowedException(String message){
        super(message);
    }
}
