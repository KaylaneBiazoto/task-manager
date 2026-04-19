package com.example.task_manager_backend.features.projects.core.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateProjectRequest(
    @NotBlank(message = "Project name is required")
    String name,
    
    @NotBlank(message = "Project description is required")
    String description
) {
}

