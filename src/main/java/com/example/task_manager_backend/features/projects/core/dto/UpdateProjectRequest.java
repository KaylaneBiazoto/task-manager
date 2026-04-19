package com.example.task_manager_backend.features.projects.core.dto;

public record UpdateProjectRequest(
    String name,
    String description
) {
}

