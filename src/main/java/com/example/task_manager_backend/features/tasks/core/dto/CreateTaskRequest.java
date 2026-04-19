package com.example.task_manager_backend.features.tasks.core.dto;

import com.example.task_manager_backend.features.tasks.domain.TaskPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record CreateTaskRequest(
    @NotBlank(message = "Title is required")
    String title,

    String description,

    @NotNull(message = "Priority is required")
    TaskPriority priority,

    LocalDate deadline,

    @NotNull(message = "Project ID is required")
    UUID projectId,

    @NotNull(message = "Assignee ID is required")
    UUID assigneeId
) {
}

