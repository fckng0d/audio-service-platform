package com.github.fckng0d.userservice.exception.user;

import java.util.UUID;

public class UserRoleAlreadyAssignedException extends RuntimeException {
    public UserRoleAlreadyAssignedException(UUID userId, String roleName) {
        super(String.format("User with ID [%s] already has the role \"%s\" assigned.", userId, roleName));
    }
}
