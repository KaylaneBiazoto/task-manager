package com.example.task_manager_backend.features.auth.usecases;

import com.example.task_manager_backend.features.auth.core.CreateUserRequest;
import com.example.task_manager_backend.features.auth.core.UserBusinessException;
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
        if (userRepository.findByEmailAndActiveTrue(request.getEmail()).isPresent()) {
            throw new UserBusinessException("Email already registered");
        }

        // Validar se username já existe (somente usuários ativos)
        if (userRepository.findByUsernameAndActiveTrue(request.getUsername()).isPresent()) {
            throw new UserBusinessException("Username already taken");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // TODO: Implement password hashing with JWT implementation
        
        // Set role with default to MEMBER
        String role = request.getRole();
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

