package com.example.task_manager_backend.features.projects.core;

import jakarta.validation.constraints.NotBlank;

public record CreateProjectRequest(
    @NotBlank(message = "Project name is required")
    String name,

    String description,

    Long ownerId
) {
}

