package com.example.task_manager_backend.features.auth.core;

import java.time.Instant;

public record UserDto(
    Long id,
    String username,
    String email,
    String role,
    Instant createdAt,
    Instant updatedAt
) {
}

