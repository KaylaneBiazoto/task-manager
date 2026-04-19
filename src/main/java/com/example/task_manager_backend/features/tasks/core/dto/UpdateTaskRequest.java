package com.example.task_manager_backend.features.tasks.core.dto;

import com.example.task_manager_backend.features.tasks.domain.TaskPriority;
import com.example.task_manager_backend.features.tasks.domain.TaskStatus;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateTaskRequest(
    String title,

    String description,

    TaskStatus status,

    TaskPriority priority,

    LocalDate deadline,

    UUID assigneeId
) {
}

