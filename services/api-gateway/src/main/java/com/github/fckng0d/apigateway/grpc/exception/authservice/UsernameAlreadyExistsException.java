package com.github.fckng0d.apigateway.grpc.exception.authservice;

public class UsernameAlreadyExistsException extends RuntimeException{
    public UsernameAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
