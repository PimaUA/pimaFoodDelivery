package com.pimaua.core.exception.custom;

public class MenuNotFoundException extends RuntimeException{

    public MenuNotFoundException(String message){
        super(message);
    }
}
