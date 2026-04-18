package com.example.task_manager_backend.features.projects.core;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProjectRequest {
    @NotBlank(message = "Project name is required")
    private String name;

    private String description;

    private Long ownerId;
}

