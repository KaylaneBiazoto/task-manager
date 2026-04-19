package com.example.task_manager_backend.features.auth.usecases;

import com.example.task_manager_backend.features.auth.core.UpdateUserRequest;
import com.example.task_manager_backend.features.auth.core.UserBusinessException;
import com.example.task_manager_backend.features.auth.core.UserNotFoundException;
import com.example.task_manager_backend.features.auth.domain.User;
import com.example.task_manager_backend.features.auth.repositories.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UpdateUserHandler {
    private final UserRepository userRepository;

    public UpdateUserHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(Long userId, UpdateUserRequest request) {
        User user = userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Update username if provided and unique
        if (request.username() != null && !request.username().isBlank()) {
            if (!request.username().equals(user.getUsername())) {
                if (userRepository.findByUsernameAndActiveTrue(request.username()).isPresent()) {
                    throw new UserBusinessException("Username already taken");
                }
                user.setUsername(request.username());
            }
        }

        // Update email if provided and unique
        if (request.email() != null && !request.email().isBlank()) {
            if (!request.email().equals(user.getEmail())) {
                if (userRepository.findByEmailAndActiveTrue(request.email()).isPresent()) {
                    throw new UserBusinessException("Email already registered");
                }
                user.setEmail(request.email());
            }
        }

        // Update password if provided
        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(request.password()); // TODO: Implement password hashing with JWT implementation
        }

        // Update role if provided
        if (request.role() != null && !request.role().isBlank()) {
            if (!request.role().equals("ADMIN") && !request.role().equals("MEMBER")) {
                throw new UserBusinessException("Invalid role. Must be ADMIN or MEMBER");
            }
            user.setRole(request.role());
        }

        return userRepository.save(user);
    }
}

