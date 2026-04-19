package com.example.task_manager_backend.features.auth.usecases;

import com.example.task_manager_backend.features.auth.core.dto.CreateUserRequest;
import com.example.task_manager_backend.features.auth.core.exception.UserBusinessException;
import com.example.task_manager_backend.features.auth.domain.User;
import com.example.task_manager_backend.features.auth.repositories.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class CreateUserHandler {
    private final UserRepository userRepository;

    public CreateUserHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(CreateUserRequest request) {
        // Validar se email já existe (somente usuários ativos)
        if (userRepository.findByEmailAndActiveTrue(request.email()).isPresent()) {
            throw new UserBusinessException("Email already registered");
        }

        // Validar se username já existe (somente usuários ativos)
        if (userRepository.findByUsernameAndActiveTrue(request.username()).isPresent()) {
            throw new UserBusinessException("Username already taken");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(request.password());

        // Set role with default to MEMBER
        String role = request.role();
        if (role == null || role.isBlank()) {
            role = "MEMBER";
        }
        
        if (!role.equals("ADMIN") && !role.equals("MEMBER")) {
            throw new UserBusinessException("Invalid role. Must be ADMIN or MEMBER");
        }
        
        user.setRole(role);
        
        return userRepository.save(user);
    }
}

