package com.example.task_manager_backend.features.auth.core.dto;

import java.util.UUID;

public record LoginResponse(
    String token,
    String email,
    String username,
    String role,
    UUID userId
) {
}
