package com.github.fckng0d.userservice.exception.role;

public class RoleAlreadyExistsException extends RuntimeException {
    public RoleAlreadyExistsException(String roleName) {
        super("Role already exists: " + roleName);
    }
}