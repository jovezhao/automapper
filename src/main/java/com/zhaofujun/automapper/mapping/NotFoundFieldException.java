package com.zhaofujun.automapper.mapping;

public class NotFoundFieldException extends Exception {
    public NotFoundFieldException(String message){
        super(message);
    }
    public NotFoundFieldException(String message,Throwable cause){
        super(message,cause);
    }
}
