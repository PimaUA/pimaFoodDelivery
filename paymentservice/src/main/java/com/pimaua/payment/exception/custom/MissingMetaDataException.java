package com.pimaua.payment.exception.custom;

public class MissingMetaDataException extends RuntimeException{

    public MissingMetaDataException(String message){
        super(message);
    }
}
