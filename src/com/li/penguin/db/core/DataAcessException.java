package com.li.penguin.db.core;

public class DataAcessException extends RuntimeException {

    public DataAcessException(String message){
        super(message);

    }

    public DataAcessException(String message, Throwable ex){
        super(message, ex);

    }
}
