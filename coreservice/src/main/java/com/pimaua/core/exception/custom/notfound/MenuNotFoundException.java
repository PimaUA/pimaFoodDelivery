package com.pimaua.core.exception.custom.notfound;

public class MenuNotFoundException extends RuntimeException{

    public MenuNotFoundException(String message){
        super(message);
    }
}
