package com.example.task_manager_backend.features.auth.core;

public record LoginResponse(
    String token,
    String email,
    String username,
    String role,
    Long userId
) {
}
