package com.example.task_manager_backend.features.projects.core;

import jakarta.validation.constraints.NotNull;

public record AddProjectMemberRequest(
    @NotNull(message = "User ID is required")
    Long userId,

    @NotNull(message = "Project role is required")
    String projectRole
) {
}

