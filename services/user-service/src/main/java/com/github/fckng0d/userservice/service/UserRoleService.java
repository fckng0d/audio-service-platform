package com.github.fckng0d.userservice.service;

import com.github.fckng0d.userservice.domain.User;
import com.github.fckng0d.userservice.domain.UserRole;
import com.github.fckng0d.userservice.dto.role.UpdateUserRoleDto;
import com.github.fckng0d.userservice.dto.role.UserRoleDto;
import com.github.fckng0d.userservice.exception.role.RoleNotFoundException;
import com.github.fckng0d.userservice.exception.role.RoleAlreadyExistsException;
import com.github.fckng0d.userservice.repositoty.UserRepository;
import com.github.fckng0d.userservice.repositoty.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserRoleService {
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;

    public UserRole getRoleByName(String userRoleName) {
        Optional<UserRole> userRole = userRoleRepository.findByName(userRoleName);

        return userRole.orElseThrow(() -> new RoleNotFoundException(userRoleName));
    }

    public UserRole createRole(UserRoleDto userRoleDto) {
        if (userRoleRepository.existsByName(userRoleDto.getName())) {
            throw new RoleAlreadyExistsException(userRoleDto.getName());
        }

        UserRole userRole = UserRole.builder()
                .name(userRoleDto.getName())
                .description(userRoleDto.getDescription())
                .build();

        return userRoleRepository.save(userRole);
    }

    public Set<User> getUsersByRoleName(String userRoleName) {
        return getRoleByName(userRoleName).getUsers();
    }

    public void updateUserRole(UpdateUserRoleDto updateUserRoleDto) {
        UserRole role = this.getRoleByName(updateUserRoleDto.getOldName());

        role.setName(updateUserRoleDto.getNewName());
        role.setDescription(updateUserRoleDto.getDescription());

        userRoleRepository.save(role);
    }

    public void deleteUserRoleByName(String name) {
        UserRole role = this.getRoleByName(name);
        Set<User> roleOwners = role.getUsers();

        for (User user : roleOwners) {
            user.getRoles().remove(role);
            userRepository.save(user);
        }

        userRoleRepository.delete(role);
    }
}
