package com.springboot.blog.exception;

public class BadRequestException extends RuntimeException{
    private String message;

    // Constructor
    public BadRequestException(String message){
        super(message);
        this.message = message;
    }
}
