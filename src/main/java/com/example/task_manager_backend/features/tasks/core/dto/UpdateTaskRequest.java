package com.example.task_manager_backend.features.tasks.core.dto;

import com.example.task_manager_backend.features.tasks.domain.TaskPriority;
import com.example.task_manager_backend.features.tasks.domain.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateTaskRequest(
    @NotBlank(message = "Title is required")
    String title,

    String description,

    @NotNull(message = "Status is required")
    TaskStatus status,

    @NotNull(message = "Priority is required")
    TaskPriority priority,

    @NotNull(message = "Deadline is required")
    LocalDate deadline,

    @NotNull(message = "Assignee ID is required")
    UUID assigneeId
) {
}

