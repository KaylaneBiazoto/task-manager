package com.example.task_manager_backend.features.auth.usecases;

import com.example.task_manager_backend.features.auth.core.exception.UserNotFoundException;
import com.example.task_manager_backend.features.auth.domain.User;
import com.example.task_manager_backend.features.auth.repositories.UserRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class GetUserByIdHandler {
    private final UserRepository userRepository;

    public GetUserByIdHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(UUID userId) {
        return userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }
}

