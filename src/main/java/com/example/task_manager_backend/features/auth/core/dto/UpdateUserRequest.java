package com.example.task_manager_backend.features.auth.core.dto;

import jakarta.validation.constraints.Email;

public record UpdateUserRequest(
    String username,

    @Email(message = "Email should be valid")
    String email,

    String password,

    String role
) {
}

