package com.example.task_manager_backend.features.projects.core.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddProjectMemberRequest(
    @NotNull(message = "User ID is required")
    UUID userId,

    @NotNull(message = "Project role is required")
    String projectRole
) {
}

