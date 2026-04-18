package com.example.task_manager_backend.features.auth.usecases;

import com.example.task_manager_backend.features.auth.core.UserNotFoundException;
import com.example.task_manager_backend.features.auth.domain.User;
import com.example.task_manager_backend.features.auth.repositories.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class DeleteUserHandler {
    private final UserRepository userRepository;

    public DeleteUserHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void execute(Long userId) {
        User user = userRepository.findByIdAndActiveTrue(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        
        user.setActive(false);
        userRepository.save(user);
    }
}

