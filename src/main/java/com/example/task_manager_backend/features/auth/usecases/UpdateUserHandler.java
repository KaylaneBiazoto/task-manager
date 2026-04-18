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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Update username if provided and unique
        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            if (!request.getUsername().equals(user.getUsername())) {
                if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                    throw new UserBusinessException("Username already taken");
                }
                user.setUsername(request.getUsername());
            }
        }

        // Update email if provided and unique
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            if (!request.getEmail().equals(user.getEmail())) {
                if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                    throw new UserBusinessException("Email already registered");
                }
                user.setEmail(request.getEmail());
            }
        }

        // Update password if provided
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(request.getPassword()); // TODO: Implement password hashing with JWT implementation
        }

        // Update role if provided
        if (request.getRole() != null && !request.getRole().isBlank()) {
            if (!request.getRole().equals("ADMIN") && !request.getRole().equals("MEMBER")) {
                throw new UserBusinessException("Invalid role. Must be ADMIN or MEMBER");
            }
            user.setRole(request.getRole());
        }

        return userRepository.save(user);
    }
}

