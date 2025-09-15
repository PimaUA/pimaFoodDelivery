package com.pimaua.core.exception.custom.notfound;

public class OpeningHoursNotFoundException extends RuntimeException{

    public OpeningHoursNotFoundException(String message){
        super(message);
    }
}
