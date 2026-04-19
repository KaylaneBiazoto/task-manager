package com.example.task_manager_backend.features.projects.core.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CreateProjectRequest(
    @NotBlank(message = "Project name is required")
    String name,

    String description,

    UUID ownerId
) {
}

