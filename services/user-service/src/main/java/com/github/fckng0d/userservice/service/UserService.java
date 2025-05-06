package com.github.fckng0d.userservice.service;


import com.github.fckng0d.userservice.domain.User;
import com.github.fckng0d.userservice.domain.UserProfile;
import com.github.fckng0d.userservice.domain.UserRole;
import com.github.fckng0d.userservice.dto.user.UserRequestDto;
import com.github.fckng0d.userservice.exception.user.EmailAlreadyExistsException;
import com.github.fckng0d.userservice.exception.user.UserNotFoundException;
import com.github.fckng0d.userservice.exception.user.UsernameAlreadyExistsException;
import com.github.fckng0d.userservice.repositoty.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private static final String DEFAULT_ROLE = "USER_ROLE";

    private final UserRepository userRepository;
    private final UserRoleService userRoleService;

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
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

    public User createUser(UserRequestDto userRequestDto) {
        UserRole userRole = userRoleService.getRoleByName(DEFAULT_ROLE);

        if (this.userExistsByUsername(userRequestDto.getUsername())) {
            throw  new UsernameAlreadyExistsException(userRequestDto.getUsername());
        }

        if (this.userExistsByEmail(userRequestDto.getEmail())) {
            throw  new EmailAlreadyExistsException(userRequestDto.getEmail());
        }

        User user = User.builder()
                .username(userRequestDto.getUsername())
                .passwordHash(userRequestDto.getPasswordHash())
                .email(userRequestDto.getEmail())
                .build();

        UserProfile userProfile = UserProfile.builder()
                .user(user)
                .build();

        user.setProfile(userProfile);
        user.getRoles().add(userRole);

        return userRepository.save(user);
    }

    public void deleteUserById(UUID id) {
        User user = this.getUserById(id);
        userRepository.delete(user);
    }

    public User updateUsername(UUID id, String username) {
        if (this.userExistsByUsername(username)) {
            throw  new UsernameAlreadyExistsException(username);
        }

        User user = this.getUserById(id);
        user.setUsername(username);
        return userRepository.save(user);
    }

    public User updatePassword(UUID id, String passwordHash) {
        User user = this.getUserById(id);
        user.setPasswordHash(passwordHash);
        return userRepository.save(user);
    }

    public User updateEmail(UUID id, String email) {
        if (this.userExistsByUsername(email)) {
            throw  new EmailAlreadyExistsException(email);
        }

        User user = this.getUserById(id);
        user.setEmail(email);
        return userRepository.save(user);
    }
}
