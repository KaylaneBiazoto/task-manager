package com.example.task_manager_backend.features.auth.services;

import com.example.task_manager_backend.features.auth.core.dto.CreateUserRequest;
import com.example.task_manager_backend.features.auth.core.dto.UpdateUserRequest;
import com.example.task_manager_backend.features.auth.core.dto.UserDto;
import com.example.task_manager_backend.features.auth.domain.User;
import com.example.task_manager_backend.features.auth.usecases.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final CreateUserHandler createUserHandler;
    private final GetUserByIdHandler getUserByIdHandler;
    private final ListUsersHandler listUsersHandler;
    private final UpdateUserHandler updateUserHandler;
    private final DeleteUserHandler deleteUserHandler;

    public UserService(
            CreateUserHandler createUserHandler,
            GetUserByIdHandler getUserByIdHandler,
            ListUsersHandler listUsersHandler,
            UpdateUserHandler updateUserHandler,
            DeleteUserHandler deleteUserHandler) {
        this.createUserHandler = createUserHandler;
        this.getUserByIdHandler = getUserByIdHandler;
        this.listUsersHandler = listUsersHandler;
        this.updateUserHandler = updateUserHandler;
        this.deleteUserHandler = deleteUserHandler;
    }

    public UserDto createUser(CreateUserRequest request) {
        User user = createUserHandler.execute(request);
        return mapToDto(user);
    }

    public UserDto getUserById(UUID userId) {
        User user = getUserByIdHandler.execute(userId);
        return mapToDto(user);
    }
    
    public Page<UserDto> listUsers(String search, String role, Pageable pageable) {
        Page<User> users = listUsersHandler.execute(search, role, pageable);
        return users.map(this::mapToDto);
    }

    public UserDto updateUser(UUID userId, UpdateUserRequest request) {
        User user = updateUserHandler.execute(userId, request);
        return mapToDto(user);
    }

    public void deleteUser(UUID userId) {
        deleteUserHandler.execute(userId);
    }

    private UserDto mapToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}



