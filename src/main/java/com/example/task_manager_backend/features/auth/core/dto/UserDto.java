package com.example.task_manager_backend.features.auth.core.dto;

import java.time.Instant;
import java.util.UUID;

public record UserDto(
    UUID id,
    String username,
    String email,
    String role,
    Instant createdAt,
    Instant updatedAt
) {
}

