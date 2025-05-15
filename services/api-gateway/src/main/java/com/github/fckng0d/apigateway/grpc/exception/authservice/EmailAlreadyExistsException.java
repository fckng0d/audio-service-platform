package com.github.fckng0d.apigateway.grpc.exception.authservice;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
