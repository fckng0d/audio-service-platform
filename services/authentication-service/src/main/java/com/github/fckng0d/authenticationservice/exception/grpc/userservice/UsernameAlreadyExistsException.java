package com.github.fckng0d.authenticationservice.exception.grpc.userservice;

public class UsernameAlreadyExistsException extends RuntimeException{
    public UsernameAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
