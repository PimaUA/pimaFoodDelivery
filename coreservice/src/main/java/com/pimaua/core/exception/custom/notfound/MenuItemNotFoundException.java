package com.pimaua.core.exception.custom.notfound;

public class MenuItemNotFoundException extends RuntimeException{

    public MenuItemNotFoundException(String message){
        super(message);
    }
}
