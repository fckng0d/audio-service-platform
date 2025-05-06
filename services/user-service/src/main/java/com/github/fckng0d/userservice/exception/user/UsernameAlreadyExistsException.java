package com.github.fckng0d.userservice.exception.user;

public class UsernameAlreadyExistsException extends RuntimeException{
    public UsernameAlreadyExistsException(String username) {
        super("Username already exists: " + username);
    }
}
