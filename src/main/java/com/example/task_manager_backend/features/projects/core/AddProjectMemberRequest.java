package com.example.task_manager_backend.features.projects.core;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddProjectMemberRequest {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Project role is required")
    private String projectRole;
}

