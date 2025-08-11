package com.pimaua.core.exception.custom;

public class MenuItemNotFoundException extends RuntimeException{

    public MenuItemNotFoundException(String message){
        super(message);
    }
}
