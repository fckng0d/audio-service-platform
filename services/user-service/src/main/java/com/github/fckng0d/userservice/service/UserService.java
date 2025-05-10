package com.github.fckng0d.userservice.service;


import com.github.fckng0d.userservice.domain.User;
import com.github.fckng0d.userservice.domain.UserProfile;
import com.github.fckng0d.userservice.domain.UserRole;
import com.github.fckng0d.userservice.dto.user.CreateUserRequestDto;
import com.github.fckng0d.userservice.exception.user.EmailAlreadyExistsException;
import com.github.fckng0d.userservice.exception.user.UserNotFoundException;
import com.github.fckng0d.userservice.exception.user.UserRoleAlreadyAssignedException;
import com.github.fckng0d.userservice.exception.user.UsernameAlreadyExistsException;
import com.github.fckng0d.userservice.grpc.client.ImageServiceGrpcClient;
import com.github.fckng0d.userservice.repositoty.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final String DEFAULT_ROLE = "LISTENER_ROLE";

    private final ImageServiceGrpcClient imageServiceGrpcClient;

    private final UserRepository userRepository;
    private final UserRoleService userRoleService;

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> UserNotFoundException.byId(id));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> UserNotFoundException.byUsername(username));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> UserNotFoundException.byEmail(email));
    }

    public Set<UserRole> getRolesById(UUID id) {
        return this.getUserById(id).getRoles();
    }

    public boolean userExistsByUsername(String username) {
        return userRepository.existsByEmail(username);
    }

    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User createUser(CreateUserRequestDto createUserRequestDto) {
        UserRole userRole = userRoleService.getRoleByName(DEFAULT_ROLE);

        if (this.userExistsByUsername(createUserRequestDto.getUsername())) {
            throw new UsernameAlreadyExistsException(createUserRequestDto.getUsername());
        }

        if (this.userExistsByEmail(createUserRequestDto.getEmail())) {
            throw new EmailAlreadyExistsException(createUserRequestDto.getEmail());
        }

        User user = User.builder()
                .username(createUserRequestDto.getUsername())
                .passwordHash(createUserRequestDto.getPasswordHash())
                .email(createUserRequestDto.getEmail())
                .build();

        UserProfile userProfile = UserProfile.builder()
                .user(user)
                .build();

        user.setProfile(userProfile);
        user.getRoles().add(userRole);

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUserById(UUID id) {
        User user = this.getUserById(id);

        Long profileImageId = user.getProfile().getImageId();

        userRepository.delete(user);

        if (profileImageId != null) {
            imageServiceGrpcClient.deleteImageById(user.getProfile().getImageId());
        }
    }

    public User updateUsername(UUID id, String username) {
        if (this.userExistsByUsername(username)) {
            throw new UsernameAlreadyExistsException(username);
        }

        User user = this.getUserById(id);
        user.setUsername(username);
        return userRepository.save(user);
    }

    public User updatePasswordHash(UUID id, String passwordHash) {
        User user = this.getUserById(id);
        user.setPasswordHash(passwordHash);
        return userRepository.save(user);
    }

    public User updateEmail(UUID id, String email) {
        if (this.userExistsByUsername(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        User user = this.getUserById(id);
        user.setEmail(email);
        return userRepository.save(user);
    }

    public User assignRole(UUID userId, String roleName) {
        User user = this.getUserById(userId);
        UserRole role = userRoleService.getRoleByName(roleName);

        if (user.getRoles().contains(role)) {
            throw new UserRoleAlreadyAssignedException(userId, roleName);
        }

        user.getRoles().add(role);
        role.getUsers().add(user);
        return userRepository.save(user);
    }
}
