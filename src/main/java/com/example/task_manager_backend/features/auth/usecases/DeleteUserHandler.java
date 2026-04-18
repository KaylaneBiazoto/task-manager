package com.example.task_manager_backend.features.auth.usecases;

import com.example.task_manager_backend.features.auth.core.UserNotFoundException;
import com.example.task_manager_backend.features.auth.repositories.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class DeleteUserHandler {
    private final UserRepository userRepository;

    public DeleteUserHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
    }
}

