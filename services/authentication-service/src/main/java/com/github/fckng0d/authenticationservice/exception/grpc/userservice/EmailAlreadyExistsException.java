package com.github.fckng0d.authenticationservice.exception.grpc.userservice;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
